package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.FittingCheckoutPaymentResultData
import ru.mercury.vpclient.shared.data.network.response.DeliveryTimeResponse
import ru.mercury.vpclient.shared.data.network.response.OrderResponse
import ru.mercury.vpclient.shared.data.network.type.OrderPaymentStatus
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun OrderResponse.entity(
    paymentStatus: OrderPaymentStatus
): FittingCheckoutPaymentResultData {
    val delivery = deliveries.orEmpty().firstOrNull()

    return FittingCheckoutPaymentResultData(
        paymentStatus = paymentStatus,
        deliveryInterval = delivery?.deliveryTime.fittingCheckoutPaymentResultInterval,
        address = delivery?.address?.address.orEmpty()
    )
}

private val DeliveryTimeResponse?.fittingCheckoutPaymentResultInterval: String
    get() {
        val fromDateTime = this?.fromValue.fittingCheckoutPaymentResultDateTime
        val toDateTime = this?.to.fittingCheckoutPaymentResultDateTime
        return when {
            fromDateTime != null && toDateTime != null -> {
                "${fromDateTime.format(paymentResultDateFormatter)} с " +
                    "${fromDateTime.format(paymentResultTimeFormatter)} до " +
                    toDateTime.format(paymentResultTimeFormatter)
            }
            else -> listOf(this?.fromValue, this?.to).filterNotNull().joinToString(" - ")
        }
    }

private val String?.fittingCheckoutPaymentResultDateTime: LocalDateTime?
    get() {
        val value = this ?: return null
        return runCatching { OffsetDateTime.parse(value).toLocalDateTime() }
            .getOrNull()
            ?: runCatching { LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }.getOrNull()
    }

private val paymentResultLocale = Locale.forLanguageTag("ru")
private val paymentResultDateFormatter = DateTimeFormatter.ofPattern("d MMMM", paymentResultLocale)
private val paymentResultTimeFormatter = DateTimeFormatter.ofPattern("HH:mm", paymentResultLocale)
