@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.CancellationException
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.FittingCheckoutPaymentResultData
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.domain.mapper.entity
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import javax.inject.Inject

// fixme
class FittingCheckoutPaymentResultUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<String, FittingCheckoutPaymentResultData>(dispatchers.io) {

    override suspend fun execute(orderNumber: String): FittingCheckoutPaymentResultData {
        val response = handleResponseResult {
            networkService.ordersByOrderId(orderId = orderNumber)
        }.getOrElse { throwable ->
            when (throwable) {
                is CancellationException -> throw throwable
                else -> throw FittingCheckoutPaymentResultException(throwable.message.orEmpty())
            }
        }
        val order = response.order
            ?: throw FittingCheckoutPaymentResultException("Не удалось определить заказ")
        val paymentStatus = order.paymentStatus
            ?: throw FittingCheckoutPaymentResultException("Не удалось определить статус оплаты")
        return order.entity(paymentStatus)
    }

    data class FittingCheckoutPaymentResultException(
        override val message: String
    ): ClientException(message)
}
