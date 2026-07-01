package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.changeQuantityRequest
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

// fixme
class SetProductQuantityUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    private val loadBasketUseCase: LoadBasketUseCase,
    dispatchers: SharedDispatchers
): UseCase<SetProductQuantityUseCase.Params, Unit>(dispatchers.io) {

    override suspend fun execute(params: Params) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = params.product.changeQuantityRequest(pairedUserId, params.quantity)
                networkService.basket(request)
            },
            onSuccess = { loadBasketUseCase(Unit).getOrThrow() },
            onEmpty = { loadBasketUseCase(Unit).getOrThrow() },
            onFailure = { error -> throw SetProductQuantityException(error.message) }
        )
    }

    data class Params(
        val product: CartProduct,
        val quantity: Int
    )

    data class SetProductQuantityException(
        override val message: String
    ): ClientException(message)
}
