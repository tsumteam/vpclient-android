package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class FittingHistoryRequest(
    @SerialName("pairedUserId") val pairedUserId: String? = null,
    @SerialName("filters") val filters: List<JsonElement> = emptyList()
)
