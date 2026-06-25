package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StatisticsResponseBaseResponse(
    @SerialName("viewsQty") val viewsQty: Int? = null,
    @SerialName("basketQty") val basketQty: Int? = null,
    @SerialName("fittingQty") val fittingQty: Int? = null,
    @SerialName("boughtQty") val boughtQty: Int? = null
)
