@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.persistence.database.dao.CartProductDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.basketShowAlternativesRequest
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

// fixme
class BasketShowAlternativesUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val cartProductDao: CartProductDao,
    private val settingsDataStore: SettingsDataStore,
    private val loadBasketUseCase: LoadBasketUseCase,
    dispatchers: SharedDispatchers
): UseCase<CartProduct, Unit>(dispatchers.io) {

    override suspend fun execute(product: CartProduct) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        cartProductDao.updateIsAlternativesPaletteOpen(product.id, true)
        handleResponse(
            request = {
                val request = product.basketShowAlternativesRequest(pairedUserId)
                networkService.basket(request)
            },
            onSuccess = { loadBasketUseCase(Unit).getOrThrow() },
            onFailure = { error -> throw BasketShowAlternativesException(error.message) }
        )
    }

    data class BasketShowAlternativesException(
        override val message: String
    ): ClientException(message)
}
