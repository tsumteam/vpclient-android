@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import androidx.room.withTransaction
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CartProductDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.cartProduct
import ru.mercury.vpclient.shared.domain.mapper.cartProductsAfterDragRequest
import ru.mercury.vpclient.shared.domain.mapper.entity
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

// fixme
class MoveProductsAfterDragUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val appDatabase: AppDatabase,
    private val cartProductDao: CartProductDao,
    private val settingsDataStore: SettingsDataStore,
    private val loadBasketUseCase: LoadBasketUseCase,
    dispatchers: SharedDispatchers
): UseCase<List<CartProduct>, Unit>(dispatchers.io) {

    override suspend fun execute(products: List<CartProduct>) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        val currentProducts = cartProductDao.selectAll().map { it.cartProduct }
        val changedLookProducts = products.filter { product ->
            currentProducts.firstOrNull { it.id == product.id }?.lookId != product.lookId
        }
        appDatabase.withTransaction {
            cartProductDao.upsert(
                products.mapIndexed { index, product ->
                    product.entity(index)
                }
            )
        }

        handleResponse(
            request = {
                val request = cartProductsAfterDragRequest(
                    products = products,
                    changedLookProducts = changedLookProducts,
                    pairedUserId = pairedUserId
                )
                networkService.basket(request)
            },
            onSuccess = { loadBasketUseCase(Unit).getOrThrow() },
            onEmpty = { loadBasketUseCase(Unit).getOrThrow() },
            onFailure = { error ->
                loadBasketUseCase(Unit).getOrThrow()
                throw MoveProductsAfterDragException(error.message)
            }
        )
    }

    data class MoveProductsAfterDragException(
        override val message: String
    ): ClientException(message)
}
