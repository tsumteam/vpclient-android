package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogProductSearchCardV2Response(
    @SerialName("id") val id: String?,
    @SerialName("itemId") val itemId: String?,
    @SerialName("colorId") val colorId: String?,
    @SerialName("name") val name: String?,
    @SerialName("price") val price: Double?,
    @SerialName("priceWithoutDiscount") val priceWithoutDiscount: Double?,
    @SerialName("brand") val brand: String?,
    @SerialName("urlBrandLogo") val urlBrandLogo: String?,
    @SerialName("imageUrl") val imageUrl: String?,
    @SerialName("imageUrls") val imageUrls: List<String>?,
    @SerialName("season") val season: String?,
    @SerialName("onlyInTransit") val onlyInTransit: Boolean?,
    @SerialName("onlyInVipSite") val onlyInVipSite: Boolean?,
    @SerialName("isGiftCard") val isGiftCard: Boolean?,
    @SerialName("actions") val actions: List<CatalogProductSearchCardV2ActionResponse>?
)
