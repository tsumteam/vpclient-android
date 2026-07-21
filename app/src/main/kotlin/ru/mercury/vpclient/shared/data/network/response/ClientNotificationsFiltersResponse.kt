package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientNotificationsFiltersResponse(
    @SerialName("filters") val filters: List<ClientNotificationFilterResponse>? = null
)
