package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LookProductsWithStatsByClientResponse(
    @SerialName("compilationName") val compilationName: String? = null,
    @SerialName("lookInfo") val lookInfo: LooksWithStatsResponseItemResponse? = null,
    @SerialName("products") val products: List<LookProductsResponseItemResponse>? = null
)
