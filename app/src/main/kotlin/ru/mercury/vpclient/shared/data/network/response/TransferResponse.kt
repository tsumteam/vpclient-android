package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransferResponse(
    @SerialName("transferId") val transferId: String? = null,
    @SerialName("transferOrderStatus") val transferOrderStatus: String? = null,
    @SerialName("transferType") val transferType: String? = null,
    @SerialName("employee") val employee: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("locationFrom") val locationFrom: String? = null,
    @SerialName("locationTo") val locationTo: String? = null,
    @SerialName("statusHistory") val statusHistory: List<TransferStatusHistoryItemResponse>? = null
)
