package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransferStatusHistoryItemResponse(
    @SerialName("time") val time: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("courier") val courier: String? = null
)
