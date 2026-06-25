package ru.mercury.vpclient.shared.data.network.type

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class OrderPaymentStatus {
    @SerialName("notPaid") NOT_PAID,
    @SerialName("bonusReservationStarted") BONUS_RESERVATION_STARTED,
    @SerialName("bonusReservationFinished") BONUS_RESERVATION_FINISHED,
    @SerialName("paymentStarted") PAYMENT_STARTED,
    @SerialName("paymentFinished") PAYMENT_FINISHED,
    @SerialName("paymentForbidden") PAYMENT_FORBIDDEN
}
