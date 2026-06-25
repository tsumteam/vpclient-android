package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageReadDtoResponse(
    @SerialName("isRead") val isRead: Boolean? = null,
    @SerialName("isReceived") val isReceived: Boolean? = null,
    @SerialName("isDeleted") val isDeleted: Boolean? = null,
    @SerialName("isEdited") val isEdited: Boolean? = null,
    @SerialName("text") val text: String? = null,
    @SerialName("messageId") val messageId: Long? = null
)
