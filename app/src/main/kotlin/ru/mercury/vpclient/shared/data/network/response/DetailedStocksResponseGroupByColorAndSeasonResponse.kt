package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailedStocksResponseGroupByColorAndSeasonResponse(
    @SerialName("colorId") val colorId: String? = null,
    @SerialName("baseColor") val baseColor: String? = null,
    @SerialName("colorHex") val colorHex: String? = null,
    @SerialName("season") val season: String? = null,
    @SerialName("rows") val rows: List<DetailedStocksResponseRowResponse>? = null
)
