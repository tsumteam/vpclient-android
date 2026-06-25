package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BasketResponse(
    @SerialName("editor") val editor: String? = null,
    @SerialName("id") val id: String? = null,
    @SerialName("lines") val lines: List<BasketLineResponse>? = null,
    @SerialName("looks") val looks: List<BasketLookResponse>? = null,
    @SerialName("catalogActionDisclaimer") val catalogActionDisclaimer: String? = null,
    @SerialName("timestamp") val timestamp: String? = null,
    @SerialName("version") val version: Int? = null
)
