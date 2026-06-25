package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteMessageRequest(
    @SerialName("pairedUserId") val pairedUserId: String? = null,
    @SerialName("messageIds") val messageIds: List<Long>? = null
)
