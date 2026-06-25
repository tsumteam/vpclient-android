package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class ErrorResponse(
    @SerialName("code") val code: Int? = null,
    @SerialName("display") val display: String? = null,
    @SerialName("msg") val msg: String? = null,
    @SerialName("reason") val reason: JsonElement? = null
)
