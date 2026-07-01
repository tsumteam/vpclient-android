package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CartProductDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.cartProductWithLook
import ru.mercury.vpclient.shared.domain.mapper.catalogFilterProductsEntity
import ru.mercury.vpclient.shared.domain.mapper.entity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

// fixme
class LoadBasketUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val cartProductDao: CartProductDao,
    private val catalogFilterProductsDao: CatalogFilterProductsDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Unit, Unit>(dispatchers.io) {

    override suspend fun execute(params: Unit) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) {
            cartProductDao.delete()
            return
        }

        handleResponse(
            request = { networkService.basketByPairedUserId(pairedUserId) },
            onSuccess = { cart ->
                val lines = cart.lines.orEmpty()
                val looks = cart.looks.orEmpty()
                val products = lines.mapNotNull { it.cartProductWithLook(looks) }
                val catalogFilterProductsEntities = products.mapIndexedNotNull { index, product ->
                    product.catalogFilterProductsEntity(index)
                }
                val cartProductEntities = products.mapIndexed { index, product ->
                    product.entity(index)
                }
                catalogFilterProductsDao.upsert(catalogFilterProductsEntities)
                appDatabase.withTransaction {
                    cartProductDao.delete()
                    cartProductDao.upsert(cartProductEntities)
                }
            }
        )
    }
}
