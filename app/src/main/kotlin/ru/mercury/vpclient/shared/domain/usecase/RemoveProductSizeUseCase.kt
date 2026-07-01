package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductSize
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.removeProductSizeRequest
import javax.inject.Inject

// fixme
class RemoveProductSizeUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    private val loadBasketUseCase: LoadBasketUseCase,
    dispatchers: SharedDispatchers
): UseCase<RemoveProductSizeUseCase.Params, Unit>(dispatchers.io) {

    override suspend fun execute(params: Params) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = params.product.removeProductSizeRequest(pairedUserId, params.size)
                networkService.basket(request)
            },
            onSuccess = { loadBasketUseCase(Unit).getOrThrow() },
            onEmpty = { loadBasketUseCase(Unit).getOrThrow() },
            onFailure = { error -> throw RemoveProductSizeException(error.message) }
        )
    }

    data class Params(
        val product: CartProduct,
        val size: CartProductSize
    )

    data class RemoveProductSizeException(
        override val message: String
    ): ClientException(message)
}
