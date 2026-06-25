package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LookProductsWithStatsResponse(
    @SerialName("lookInfo") val lookInfo: LooksWithStatsResponseItemResponse? = null,
    @SerialName("products") val products: List<LookByProductsResponseItemResponse>? = null,
    @SerialName("clients") val clients: List<LookByClientsResponseItemResponse>? = null
)
