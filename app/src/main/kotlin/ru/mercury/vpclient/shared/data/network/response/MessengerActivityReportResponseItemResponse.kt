package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.MessageType

@Serializable
data class MessengerActivityReportResponseItemResponse(
    @SerialName("creationDate") val creationDate: String? = null,
    @SerialName("clientId") val clientId: String? = null,
    @SerialName("employeeId") val employeeId: String? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("sender") val sender: MessageType? = null,
    @SerialName("isRead") val isRead: Boolean? = null,
    @SerialName("messageId") val messageId: Long? = null,
    @SerialName("readTime") val readTime: String? = null
)
