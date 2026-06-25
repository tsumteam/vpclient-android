package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompilationsWithStatsByClientsResponseItemResponse(
    @SerialName("statistics") val statistics: StatsByClientsResponse? = null,
    @SerialName("id") val id: Int? = null,
    @SerialName("collageUrl") val collageUrl: String? = null,
    @SerialName("photoUrl") val photoUrl: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("createDate") val createDate: String? = null,
    @SerialName("looksQty") val looksQty: Int? = null,
    @SerialName("lookProductsQty") val lookProductsQty: Int? = null,
    @SerialName("isStatsAvailable") val isStatsAvailable: Boolean? = null
)
