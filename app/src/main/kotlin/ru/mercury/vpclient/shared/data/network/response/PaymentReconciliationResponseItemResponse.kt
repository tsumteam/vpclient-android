package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentReconciliationResponseItemResponse(
    @SerialName("acquiringOrderCode") val acquiringOrderCode: String? = null,
    @SerialName("transactionDate") val transactionDate: String? = null,
    @SerialName("paymentType") val paymentType: String? = null,
    @SerialName("paymentSource") val paymentSource: Int? = null,
    @SerialName("orderNumber") val orderNumber: String? = null,
    @SerialName("orderAmount") val orderAmount: Double? = null
)
