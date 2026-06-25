package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OrderExternalPushResponseItemResponse(
    @SerialName("messageId") val messageId: String? = null,
    @SerialName("status") val status: Int? = null,
    @SerialName("errorMessage") val errorMessage: String? = null
)
