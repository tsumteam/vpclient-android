package ru.mercury.vpclient.shared.domain.usecase

import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.fittingPaySwitchRequest
import ru.mercury.vpclient.shared.domain.mapper.handleResponse
import javax.inject.Inject

// fixme
class ChangeFittingPaySwitchUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<ChangeFittingPaySwitchUseCase.Params, Unit>(dispatchers.io) {

    override suspend fun execute(params: Params) {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser).orEmpty()
        if (pairedUserId.isEmpty()) return

        handleResponse(
            request = {
                val request = params.product.fittingPaySwitchRequest(pairedUserId, params.paySwitch)
                networkService.fittingsAddOperations(request)
            },
            onSuccess = {},
            onFailure = { error -> throw ChangeFittingPaySwitchException(error.message) }
        )
    }

    data class Params(
        val product: CartProduct,
        val paySwitch: Boolean
    )

    data class ChangeFittingPaySwitchException(
        override val message: String
    ): ClientException(message)
}
