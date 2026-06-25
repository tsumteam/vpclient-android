package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class LooksResponseItemDtoResponse(
    @SerialName("id") val id: Int? = null,
    @SerialName("collageUrl") val collageUrl: String? = null,
    @SerialName("photoUrl") val photoUrl: String? = null,
    @SerialName("meta") val meta: JsonElement? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("createDate") val createDate: String? = null,
    @SerialName("lookProductsQty") val lookProductsQty: Int? = null,
    @SerialName("isStatsAvailable") val isStatsAvailable: Boolean? = null
)
