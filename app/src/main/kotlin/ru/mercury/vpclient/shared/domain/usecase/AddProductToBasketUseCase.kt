package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogFilterProductsDao
import ru.mercury.vpclient.shared.data.persistence.database.dao.CatalogViewHistoryProductDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.addProductToBasketRequest
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

private const val ADD_PRODUCT_TO_BASKET_ERROR_MESSAGE = "Не удалось добавить товар в корзину"

// fixme
class AddProductToBasketUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val catalogFilterProductsDao: CatalogFilterProductsDao,
    private val catalogViewHistoryProductDao: CatalogViewHistoryProductDao,
    private val settingsDataStore: SettingsDataStore,
    private val loadBasketUseCase: LoadBasketUseCase,
    dispatchers: SharedDispatchers
): UseCase<AddProductToBasketUseCase.Params, Unit>(dispatchers.io) {

    override suspend fun execute(params: Params) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        val product = catalogFilterProductsDao.select(params.productId)
            ?: catalogViewHistoryProductDao.selectCatalogProduct(params.productId)
            ?: throw AddProductToBasketException(ADD_PRODUCT_TO_BASKET_ERROR_MESSAGE)

        handleResponse(
            request = {
                val request = product.addProductToBasketRequest(pairedUserId, params.sizeId)
                networkService.basket(request)
            },
            onSuccess = { loadBasketUseCase(Unit).getOrThrow() },
            onEmpty = { loadBasketUseCase(Unit).getOrThrow() },
            onFailure = { error -> throw AddProductToBasketException(error.message) }
        )
    }

    data class Params(
        val productId: String,
        val sizeId: String?
    )

    data class AddProductToBasketException(
        override val message: String
    ): ClientException(message)
}
