package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvailableSizesResponseItemResponse(
    @SerialName("sizeFullName") val sizeFullName: String? = null,
    @SerialName("sizeId") val sizeId: String? = null,
    @SerialName("inOrder") val inOrder: Boolean? = null,
    @SerialName("inStock") val inStock: Boolean? = null,
    @SerialName("inStockShops") val inStockShops: List<String>? = null,
    @SerialName("isOnlyInVipSite") val isOnlyInVipSite: Boolean? = null,
    @SerialName("isOnlyInTransit") val isOnlyInTransit: Boolean? = null
)
