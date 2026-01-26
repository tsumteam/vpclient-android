package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentSbpResponse(
    @SerialName("paymentId") val paymentId: String?,
    @SerialName("qrCodeUrl") val qrCodeUrl: String?,
    @SerialName("qrCodeBase64") val qrCodeBase64: String?
)
