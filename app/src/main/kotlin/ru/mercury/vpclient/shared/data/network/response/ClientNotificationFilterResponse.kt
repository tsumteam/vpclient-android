package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class ClientNotificationFilterResponse(
    @SerialName("filterType") val filterType: String? = null,
    @SerialName("order") val order: Int? = null,
    @SerialName("label") val label: String? = null,
    @SerialName("values") val values: List<JsonObject>? = null
)
