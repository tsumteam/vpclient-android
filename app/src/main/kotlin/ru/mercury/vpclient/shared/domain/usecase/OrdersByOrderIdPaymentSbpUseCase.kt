@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.CancellationException
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import javax.inject.Inject

// fixme
class OrdersByOrderIdPaymentSbpUseCase @Inject constructor(
    private val networkService: NetworkService,
    dispatchers: SharedDispatchers
): UseCase<String, String>(dispatchers.io) {

    override suspend fun execute(orderId: String): String {
        val response = handleResponseResult {
            networkService.ordersByOrderIdPaymentSbp(orderId)
        }.getOrElse { throwable ->
            when (throwable) {
                is CancellationException -> throw throwable
                else -> throw OrdersByOrderIdPaymentSbpException(throwable.message.orEmpty())
            }
        }
        return response.qrCodeUrl
            ?.takeIf(String::isNotBlank)
            ?: throw OrdersByOrderIdPaymentSbpException("Не удалось получить ссылку на оплату")
    }

    data class OrdersByOrderIdPaymentSbpException(
        override val message: String
    ): ClientException(message)
}
