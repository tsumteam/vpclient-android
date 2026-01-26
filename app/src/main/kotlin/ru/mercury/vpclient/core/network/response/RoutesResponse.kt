package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoutesResponse(
    @SerialName("routes") val routes: List<RouteResponse>
)
