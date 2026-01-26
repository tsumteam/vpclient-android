package ru.mercury.vpclient.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SbpRefundRequest(
    @SerialName("paymentId") val paymentId: String,
    @SerialName("amount") val amount: Double,
    @SerialName("trxId") val trxId: String
)
