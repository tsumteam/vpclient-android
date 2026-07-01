package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.persistence.database.dao.CartProductDao
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import ru.mercury.vpclient.shared.domain.mapper.paySwitchRequest
import javax.inject.Inject

// fixme
class ChangePaySwitchUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val cartProductDao: CartProductDao,
    private val settingsDataStore: SettingsDataStore,
    private val loadBasketUseCase: LoadBasketUseCase,
    dispatchers: SharedDispatchers
): UseCase<ChangePaySwitchUseCase.Params, Unit>(dispatchers.io) {

    override suspend fun execute(params: Params) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        cartProductDao.updateIsForPayment(params.product.id, params.paySwitch)

        handleResponse(
            request = {
                val request = params.product.paySwitchRequest(pairedUserId, params.paySwitch)
                networkService.basket(request)
            },
            onSuccess = { loadBasketUseCase(Unit).getOrThrow() },
            onEmpty = { loadBasketUseCase(Unit).getOrThrow() },
            onFailure = { error ->
                loadBasketUseCase(Unit).getOrThrow()
                throw ChangePaySwitchException(error.message)
            }
        )
    }

    data class Params(
        val product: CartProduct,
        val paySwitch: Boolean
    )

    data class ChangePaySwitchException(
        override val message: String
    ): ClientException(message)
}
