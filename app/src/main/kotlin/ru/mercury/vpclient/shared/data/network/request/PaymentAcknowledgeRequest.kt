package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentAcknowledgeRequest(
    @SerialName("setId") val setId: String? = null,
    @SerialName("paymentType") val paymentType: String? = null,
    @SerialName("orderAcq") val orderAcq: String? = null,
    @SerialName("bonusAmount") val bonusAmount: Double? = null,
    @SerialName("discountAmount") val discountAmount: Double? = null,
    @SerialName("cashAmount") val cashAmount: Double? = null,
    @SerialName("bonusType") val bonusType: String? = null,
    @SerialName("rrn") val rrn: String? = null,
    @SerialName("paymentCardType") val paymentCardType: String? = null
)
