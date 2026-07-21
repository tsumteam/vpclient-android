package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientNotificationsResponse(
    @SerialName("items") val items: List<ClientNotificationResponse>? = null,
    @SerialName("paginationToken") val paginationToken: String? = null
)
