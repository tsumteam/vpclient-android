package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.response.ClientCompilationPayloadResponse
import ru.mercury.vpclient.shared.data.network.type.MessageType

@Serializable
data class SaveClientCompilationMessageRequest(
    @SerialName("pairedUserId") val pairedUserId: String? = null,
    @SerialName("createTime") val createTime: String? = null,
    @SerialName("localCreateTime") val localCreateTime: String? = null,
    @SerialName("localMessageId") val localMessageId: String? = null,
    @SerialName("payload") val payload: ClientCompilationPayloadResponse? = null,
    @SerialName("showBadge") val showBadge: Boolean? = null,
    @SerialName("showChecks") val showChecks: Boolean? = null,
    @SerialName("type") val type: MessageType? = null,
    @SerialName("isRead") val isRead: Boolean? = null,
    @SerialName("isReceived") val isReceived: Boolean? = null,
    @SerialName("isDeleted") val isDeleted: Boolean? = null,
    @SerialName("isEdited") val isEdited: Boolean? = null,
    @SerialName("text") val text: String? = null,
    @SerialName("messageId") val messageId: Long? = null
)
