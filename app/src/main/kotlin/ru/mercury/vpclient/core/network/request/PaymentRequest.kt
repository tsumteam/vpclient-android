package ru.mercury.vpclient.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentRequest(
    @SerialName("paymentId") val paymentId: String,
    @SerialName("paymentType") val paymentType: String,
    @SerialName("amount") val amount: Double,
    @SerialName("loyaltyCardNumber") val loyaltyCardNumber: String?,
    @SerialName("smsVerifyCode") val smsVerifyCode: String?,
    @SerialName("sourceId") val sourceId: String?
)
