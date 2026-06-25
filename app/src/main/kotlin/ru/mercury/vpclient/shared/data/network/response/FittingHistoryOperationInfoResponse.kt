package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FittingHistoryOperationInfoResponse(
    @SerialName("operationType") val operationType: String? = null,
    @SerialName("operationTypeColorHex") val operationTypeColorHex: String? = null,
    @SerialName("timestamp") val timestamp: String? = null
)
