package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.Serializable

@Serializable
data class CatalogProductDetailCardV2ColorResponse(
    val colorId: String? = null,
    val colorName: String? = null,
    val colorHex: String? = null,
    val imageUrls: List<String>? = null,
    val season: String? = null,
    val artDescription: String? = null,
    val areStocksAvailable: Boolean? = null,
    val isSeasonDisplay: Boolean? = null,
    val isSelected: Boolean? = null,
    val oneSize: Boolean? = null,
    val price: Double? = null,
    val priceWithoutDiscount: Double? = null,
    val actions: List<CatalogProductDetailCardV2ActionResponse>? = null,
    val urlItemVideo: String? = null,
    val availableSizes: CatalogProductDetailCardV2AvailableSizesResponse? = null,
    val wearWith: List<CatalogProductSearchCardV2Response>? = null,
    val kits: List<CatalogProductSearchCardV2Response>? = null
)
