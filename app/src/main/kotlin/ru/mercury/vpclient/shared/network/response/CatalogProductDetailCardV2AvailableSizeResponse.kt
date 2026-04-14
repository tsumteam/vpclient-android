package ru.mercury.vpclient.shared.network.response

import kotlinx.serialization.Serializable

@Serializable
data class CatalogProductDetailCardV2AvailableSizeResponse(
    val sizeId: String? = null,
    val sizeFullName: String? = null,
    val russianSize: String? = null,
    val inOrder: Boolean? = null,
    val inStock: Boolean? = null,
    val inStockShops: List<String>? = null,
    val isOnlyInVipSite: Boolean? = null,
    val isOnlyInTransit: Boolean? = null,
    val hasStockSubscriptions: Boolean? = null,
    val russianSizeId: Int? = null
)
