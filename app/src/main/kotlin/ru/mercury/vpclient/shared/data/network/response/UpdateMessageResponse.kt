package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.MessageReadType

@Serializable
data class UpdateMessageResponse(
    @SerialName("pairedUserId") val pairedUserId: String? = null,
    @SerialName("messageIds") val messageIds: List<Long>? = null,
    @SerialName("readType") val readType: MessageReadType? = null
)
