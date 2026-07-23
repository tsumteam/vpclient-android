package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DigineticaSearchEventRequest(
    @SerialName("apiKey") val apiKey: String,
    @SerialName("channel") val channel: String,
    @SerialName("sessionId") val sessionId: String,
    @SerialName("userGUID") val userGuid: String,
    @SerialName("viewGUID") val viewGuid: String,
    @SerialName("eventType") val eventType: String,
    @SerialName("pageNumber") val pageNumber: String,
    @SerialName("pageProducts") val pageProducts: List<String>,
    @SerialName("searchTerm") val searchTerm: String
)
