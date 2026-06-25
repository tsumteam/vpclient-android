package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class Response(
    @SerialName("error") val error: ErrorResponse? = null,
    @SerialName("data") val data: JsonElement? = null
)
