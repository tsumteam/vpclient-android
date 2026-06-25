package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditMessageRequest(
    @SerialName("pairedUserId") val pairedUserId: String? = null,
    @SerialName("messageId") val messageId: Long? = null,
    @SerialName("text") val text: String? = null
)
