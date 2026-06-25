package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogProductSearchCardVNResponse(
    @SerialName("oneSize") val oneSize: Boolean? = null,
    @SerialName("article") val article: String? = null,
    @SerialName("brand") val brand: String? = null,
    @SerialName("colorId") val colorId: String? = null,
    @SerialName("colorName") val colorName: String? = null,
    @SerialName("eKttId") val eKttId: String? = null,
    @SerialName("id") val id: String? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("imageUrls") val imageUrls: List<String>? = null,
    @SerialName("isSeasonDisplay") val isSeasonDisplay: Boolean? = null,
    @SerialName("itemId") val itemId: String? = null,
    @SerialName("lookId") val lookId: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("order") val order: Int? = null,
    @SerialName("paySwitch") val paySwitch: Boolean? = null,
    @SerialName("price") val price: Double? = null,
    @SerialName("priceWithoutDiscount") val priceWithoutDiscount: Double? = null,
    @SerialName("quantity") val quantity: Int? = null,
    @SerialName("season") val season: String? = null,
    @SerialName("sizes") val sizes: List<SizeInProductSearchVNResponse>? = null
)
