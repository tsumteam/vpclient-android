package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogProductDetailCardV2AvailableSizeResponse(
    @SerialName("sizeId") val sizeId: String? = null,
    @SerialName("sizeFullName") val sizeFullName: String? = null,
    @SerialName("russianSize") val russianSize: String? = null,
    @SerialName("inOrder") val inOrder: Boolean? = null,
    @SerialName("inStock") val inStock: Boolean? = null,
    @SerialName("inStockShops") val inStockShops: List<String>? = null,
    @SerialName("isOnlyInVipSite") val isOnlyInVipSite: Boolean? = null,
    @SerialName("isOnlyInTransit") val isOnlyInTransit: Boolean? = null,
    @SerialName("hasStockSubscriptions") val hasStockSubscriptions: Boolean? = null,
    @SerialName("russianSizeId") val russianSizeId: Int? = null
)
