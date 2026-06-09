package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogProductDetailCardV2ColorResponse(
    @SerialName("colorId") val colorId: String? = null,
    @SerialName("colorName") val colorName: String? = null,
    @SerialName("colorHex") val colorHex: String? = null,
    @SerialName("imageUrls") val imageUrls: List<String>? = null,
    @SerialName("season") val season: String? = null,
    @SerialName("artDescription") val artDescription: String? = null,
    @SerialName("areStocksAvailable") val areStocksAvailable: Boolean? = null,
    @SerialName("isSeasonDisplay") val isSeasonDisplay: Boolean? = null,
    @SerialName("isSelected") val isSelected: Boolean? = null,
    @SerialName("oneSize") val oneSize: Boolean? = null,
    @SerialName("price") val price: Double? = null,
    @SerialName("priceWithoutDiscount") val priceWithoutDiscount: Double? = null,
    @SerialName("actions") val actions: List<CatalogProductDetailCardV2ActionResponse>? = null,
    @SerialName("urlItemVideo") val urlItemVideo: String? = null,
    @SerialName("availableSizes") val availableSizes: CatalogProductDetailCardV2AvailableSizesResponse? = null,
    @SerialName("hasWearWith") val hasWearWith: Boolean? = null,
    @SerialName("wearWithButtonEnabled") val wearWithButtonEnabled: Boolean? = null,
    @SerialName("wearWith") val wearWith: List<CatalogProductSearchCardV2Response>? = null,
    @SerialName("kits") val kits: List<CatalogProductSearchCardV2Response>? = null
)
