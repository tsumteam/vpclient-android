@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.basketReturnOriginalRequest
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

// fixme
class BasketReturnOriginalUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    private val loadBasketUseCase: LoadBasketUseCase,
    dispatchers: SharedDispatchers
): UseCase<CartProduct, Unit>(dispatchers.io) {

    override suspend fun execute(product: CartProduct) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = product.basketReturnOriginalRequest(pairedUserId)
                networkService.basket(request)
            },
            onSuccess = { loadBasketUseCase(Unit).getOrThrow() },
            onEmpty = { loadBasketUseCase(Unit).getOrThrow() },
            onFailure = { error -> throw BasketReturnOriginalException(error.message) }
        )
    }

    data class BasketReturnOriginalException(
        override val message: String
    ): ClientException(message)
}
