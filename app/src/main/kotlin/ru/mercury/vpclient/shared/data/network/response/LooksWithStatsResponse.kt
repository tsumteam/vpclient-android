package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LooksWithStatsResponse(
    @SerialName("compilationInfo") val compilationInfo: CompilationsWithStatsResponseItemResponse? = null,
    @SerialName("looks") val looks: List<LooksWithStatsResponseItemResponse>? = null
)
