package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImportResultResponse(
    @SerialName("productId") val productId: String? = null,
    @SerialName("plu") val plu: String? = null,
    @SerialName("colorId") val colorId: String? = null,
    @SerialName("availCatalog") val availCatalog: Boolean? = null,
    @SerialName("availQuantity") val availQuantity: Boolean? = null,
    @SerialName("fashionImageId") val fashionImageId: Int? = null
)
