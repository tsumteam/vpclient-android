package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class FilterValuesResponse(
    @SerialName("filterValues") val filterValues: List<JsonElement>?
)
