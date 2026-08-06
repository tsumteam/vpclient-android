package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.CancellationException
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.OrderCreationFromFittingRequest
import ru.mercury.vpclient.shared.data.network.type.PaymentType
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import ru.mercury.vpclient.shared.domain.usecase.OrdersCreateFromFittingUseCase.Params
import javax.inject.Inject

// fixme
class OrdersCreateFromFittingUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<Params, String>(dispatchers.io) {

    override suspend fun execute(params: Params): String {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser)
            ?.takeIf(String::isNotBlank)
            ?: throw OrdersCreateFromFittingException("Не удалось определить пользователя")
        val order = handleResponseResult(
            request = {
                val request = OrderCreationFromFittingRequest(
                    pairedUserId = pairedUserId,
                    deliveryIds = params.deliveryIds,
                    paymentType = params.paymentType,
                    ignoreCashDeskActions = null
                )
                networkService.ordersCreateFromFitting(request)
            }
        ).getOrElse { throwable ->
            when (throwable) {
                is CancellationException -> throw throwable
                else -> throw OrdersCreateFromFittingException(throwable.message.orEmpty())
            }
        }
        return order.orderNumber
            ?.takeIf(String::isNotBlank)
            ?: throw OrdersCreateFromFittingException("Не удалось определить заказ")
    }

    data class Params(
        val deliveryIds: List<String>,
        val paymentType: PaymentType
    )

    data class OrdersCreateFromFittingException(
        override val message: String
    ): ClientException(message)
}
