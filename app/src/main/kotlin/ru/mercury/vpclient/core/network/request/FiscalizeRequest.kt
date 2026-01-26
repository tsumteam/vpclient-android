package ru.mercury.vpclient.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FiscalizeRequest(
    @SerialName("orderId") val orderId: String,
    @SerialName("payments") val payments: List<PaymentRequest>,
    @SerialName("orderLines") val orderLines: List<OrderLineBarcodeRequest>
)
