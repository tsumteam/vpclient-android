package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientNotificationResponse(
    @SerialName("badge") val badge: Int? = null,
    @SerialName("id") val id: Int? = null,
    @SerialName("type") val type: Int? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("deepLinkUrl") val deepLinkUrl: String? = null,
    @SerialName("timestamp") val timestamp: String? = null
)
