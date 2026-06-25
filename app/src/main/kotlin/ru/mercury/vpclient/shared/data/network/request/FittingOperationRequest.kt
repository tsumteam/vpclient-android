package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class FittingOperationRequest(
    @SerialName("pairedUserId") val pairedUserId: String? = null,
    @SerialName("items") val items: List<JsonElement> = emptyList()
)
