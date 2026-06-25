package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class MyClientsFiltersResponse(
    @SerialName("filters") val filters: List<JsonElement>? = null
)
