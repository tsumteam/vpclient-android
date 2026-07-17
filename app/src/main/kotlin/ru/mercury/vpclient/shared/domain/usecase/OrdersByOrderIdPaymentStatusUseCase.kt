@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.CancellationException
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.type.OrderPaymentStatus
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import javax.inject.Inject

class OrdersByOrderIdPaymentStatusUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<String, OrderPaymentStatus>(dispatchers.io) {

    override suspend fun execute(orderNumber: String): OrderPaymentStatus {
        val order = handleResponseResult {
            networkService.ordersByOrderId(orderId = orderNumber)
        }.getOrElse { throwable ->
            when (throwable) {
                is CancellationException -> throw throwable
                else -> throw OrdersByOrderIdPaymentStatusException(throwable.message.orEmpty())
            }
        }
        return order.order?.paymentStatus
            ?: throw OrdersByOrderIdPaymentStatusException("Не удалось определить статус оплаты")
    }

    data class OrdersByOrderIdPaymentStatusException(
        override val message: String
    ): ClientException(message)
}
