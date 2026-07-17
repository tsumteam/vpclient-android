@file:Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")

package ru.mercury.vpclient.shared.domain.usecase

import kotlinx.coroutines.CancellationException
import ru.mercury.vpclient.shared.coroutines.SharedDispatchers
import ru.mercury.vpclient.shared.data.entity.GiftCardPaymentData
import ru.mercury.vpclient.shared.data.entity.GiftCardPaymentLinkData
import ru.mercury.vpclient.shared.data.network.NetworkService
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.request.CreateOrderWithGiftCardRequest
import ru.mercury.vpclient.shared.data.network.type.PaymentType
import ru.mercury.vpclient.shared.data.persistence.datastore.PreferenceKey
import ru.mercury.vpclient.shared.data.persistence.datastore.SettingsDataStore
import ru.mercury.vpclient.shared.domain.mapper.handleResponseResult
import javax.inject.Inject

class OrdersCreateWithGiftCardPaymentLinkUseCase @Inject constructor(
    private val networkService: NetworkService,
    private val settingsDataStore: SettingsDataStore,
    dispatchers: SharedDispatchers
): UseCase<GiftCardPaymentData, GiftCardPaymentLinkData>(dispatchers.io) {

    override suspend fun execute(paymentData: GiftCardPaymentData): GiftCardPaymentLinkData {
        val pairedUserId = settingsDataStore.getValue(PreferenceKey.PairedUser)
        val order = handleResponseResult(
            request = {
                val request = CreateOrderWithGiftCardRequest(
                    pairedUserId = pairedUserId?.takeIf { value -> value.isNotEmpty() },
                    templateId = paymentData.templateId,
                    itemId = paymentData.itemId,
                    amount = paymentData.amount,
                    type = paymentData.type,
                    email = paymentData.email,
                    phone = paymentData.phone,
                    dateFrom = paymentData.dateFrom,
                    dateTo = paymentData.dateTo,
                    paymentType = PaymentType.ONLINE_PAYMENT_CLIENT
                )
                networkService.ordersCreateWithGiftCard(request)
            }
        ).getOrElse { throwable ->
            when (throwable) {
                is CancellationException -> throw throwable
                else -> throw OrdersCreateWithGiftCardPaymentLinkException(throwable.message.orEmpty())
            }
        }
        val orderNumber = order.orderNumber?.takeIf { value -> value.isNotEmpty() }
            ?: throw OrdersCreateWithGiftCardPaymentLinkException("Не удалось определить заказ")
        val paymentLink = handleResponseResult {
            networkService.ordersByOrderIdPaymentLink(orderNumber)
        }.getOrElse { throwable ->
            when (throwable) {
                is CancellationException -> throw throwable
                else -> throw OrdersCreateWithGiftCardPaymentLinkException(throwable.message.orEmpty())
            }
        }
        val url = paymentLink.urlPayment?.takeIf { value -> value.isNotEmpty() }
            ?: throw OrdersCreateWithGiftCardPaymentLinkException("Не удалось получить ссылку на оплату")
        return GiftCardPaymentLinkData(
            orderNumber = orderNumber,
            url = url
        )
    }

    data class OrdersCreateWithGiftCardPaymentLinkException(
        override val message: String
    ): ClientException(message)
}
