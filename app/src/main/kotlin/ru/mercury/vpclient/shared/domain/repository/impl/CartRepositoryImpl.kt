package ru.mercury.vpclient.shared.domain.repository.impl

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.entity.ActivityCounterTypeRequestEnum
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CartProductDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.cartProduct
import ru.mercury.vpclient.shared.domain.mapper.catalogFilterProductsEntity
import ru.mercury.vpclient.shared.domain.mapper.changeSizeRequest
import ru.mercury.vpclient.shared.domain.mapper.entity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import ru.mercury.vpclient.shared.domain.mapper.paySwitchRequest
import ru.mercury.vpclient.shared.domain.repository.CartRepository
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val cartProductDao: CartProductDao,
    private val catalogFilterProductsDao: CatalogFilterProductsDao,
    private val settingsDataStore: SettingsDataStore
): CartRepository {

    override val cartProductsFlow: Flow<List<CartProduct>> = cartProductDao.selectAllFlow()
        .map { entities -> entities.map { it.cartProduct } }

    override val cartSize: Flow<Int> = cartProductDao.cartSizeFlow()

    override suspend fun loadCartProducts() {
        loadBasket()
    }

    override suspend fun loadBasket() {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) {
            cartProductDao.delete()
            return
        }

        handleResponse(
            request = { networkService.basketByPairedUserId(pairedUserId) },
            onSuccess = { cart ->
                val lines = cart.lines.orEmpty()
                val products = lines.mapNotNull { it.cartProduct }
                val catalogFilterProductsEntities = products.mapIndexedNotNull { index, product -> product.catalogFilterProductsEntity(index) }
                val cartProductEntities = products.mapIndexed { index, product -> product.entity(index) }
                catalogFilterProductsDao.upsert(catalogFilterProductsEntities)
                appDatabase.withTransaction {
                    cartProductDao.run {
                        delete()
                        upsert(cartProductEntities)
                    }
                }
            }
        )
    }

    override suspend fun changePaySwitch(product: CartProduct, paySwitch: Boolean) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) {
            return
        }

        cartProductDao.updateIsForPayment(product.id, paySwitch)
        try {
            handleResponseResult {
                networkService.basket(product.paySwitchRequest(pairedUserId, paySwitch))
            }.getOrThrow()
        } finally {
            loadCartProducts()
        }
    }

    override suspend fun setProductSize(product: CartProduct, sizeId: String) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        cartProductDao.updateSize(product.id, sizeId)
        try {
            handleResponseResult {
                networkService.basket(product.changeSizeRequest(pairedUserId, sizeId))
            }.getOrThrow()
        } finally {
            loadCartProducts()
        }
    }

    override suspend fun cartBadge(): Int {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) {
            return 0
        }

        val counters = handleResponseResult {
            networkService.activityCountersByPairedUserId(pairedUserId)
        }.getOrThrow()

        return counters.items.orEmpty()
            .firstOrNull { it.type == ActivityCounterTypeRequestEnum.BASKET }
            ?.value ?: 0
    }
}
