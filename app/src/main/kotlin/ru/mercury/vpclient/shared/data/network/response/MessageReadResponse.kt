package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageReadResponse(
    @SerialName("items") val items: List<MessageReadDtoResponse>? = null,
    @SerialName("revision") val revision: String? = null
)
