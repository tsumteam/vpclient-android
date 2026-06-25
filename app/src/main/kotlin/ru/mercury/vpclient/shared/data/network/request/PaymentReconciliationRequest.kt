package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentReconciliationRequest(
    @SerialName("dateFrom") val dateFrom: String? = null,
    @SerialName("dateTo") val dateTo: String? = null
)
