@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.error.AddProductToBasketException
import ru.mercury.vpclient.shared.data.error.DeleteProductException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.persistence.database.AppDatabase
import ru.mercury.vpclient.shared.data.persistence.database.dao.CartProductDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.data.persistence.database.entity.CartProductEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.addProductToBasketRequest
import ru.mercury.vpclient.shared.domain.mapper.cartProduct
import ru.mercury.vpclient.shared.domain.mapper.deleteProductRequest
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.optimisticCartProductEntity
import java.util.UUID
import javax.inject.Inject

class ToggleBasketProductUseCase @Inject constructor(
    private val appDatabase: AppDatabase,
    private val networkService: NetworkService,
    private val cartProductDao: CartProductDao,
    private val catalogFilterProductsDao: CatalogFilterProductsDao,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<CatalogFilterProductsEntity, Unit>(dispatchers.io) {

    override suspend fun execute(product: CatalogFilterProductsEntity) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        val currentCartProductEntity = cartProductDao.selectByProductKey(
            itemId = product.itemId,
            colorId = product.colorId
        )

        when {
            currentCartProductEntity == null -> addProduct(product, pairedUserId)
            else -> deleteProduct(currentCartProductEntity, pairedUserId)
        }
    }

    private suspend fun addProduct(
        product: CatalogFilterProductsEntity,
        pairedUserId: String
    ) {
        val lineId = UUID.randomUUID().toString()
        val optimisticEntity = product.optimisticCartProductEntity(
            position = cartProductDao.selectAll().size,
            lineId = lineId
        )
        cartProductDao.upsert(listOf(optimisticEntity))

        handleResponse(
            request = {
                val request = product.addProductToBasketRequest(
                    pairedUserId = pairedUserId,
                    sizeId = null,
                    lineId = lineId
                )
                networkService.basket(request)
            },
            onFailure = { error ->
                cartProductDao.delete(optimisticEntity.id)
                throw AddProductToBasketException(error.message)
            }
        )
    }

    private suspend fun deleteProduct(
        productEntity: CartProductEntity,
        pairedUserId: String
    ) {
        cartProductDao.delete(productEntity.id)

        handleResponse(
            request = {
                val request = productEntity.cartProduct.deleteProductRequest(pairedUserId)
                networkService.basket(request)
            },
            onFailure = { error ->
                cartProductDao.upsert(listOf(productEntity))
                throw DeleteProductException(error.message)
            }
        )
    }
}
