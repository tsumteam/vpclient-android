package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentMethodResponse(
    @SerialName("paymentId") val paymentId: String?,
    @SerialName("paymentName") val paymentName: String?,
    @SerialName("paymentType") val paymentType: String?
) {
    companion object {
        const val PAYMENT_ID_CARD = "БКарта"
        const val PAYMENT_ID_BONUS_MERCURY = "БонусMercury"
        const val PAYMENT_ID_CREDIT_MAESTRO = "КредиткаМаэстро"
        const val PAYMENT_ID_CASH = "Р1"
        const val PAYMENT_ID_SBP = "СБП"
    }
}
