package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailedStocksResponse(
    @SerialName("availableRemains") val availableRemains: Int? = null,
    @SerialName("groups") val groups: List<DetailedStocksResponseGroupByColorAndSeasonResponse>? = null
)
