package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogProductScanHistoryCardResponse(
    @SerialName("brand") val brand: String? = null,
    @SerialName("colorId") val colorId: String? = null,
    @SerialName("colorName") val colorName: String? = null,
    @SerialName("eKttId") val eKttId: String? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("imageUrls") val imageUrls: List<String>? = null,
    @SerialName("itemId") val itemId: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("order") val order: Int? = null,
    @SerialName("price") val price: Double? = null,
    @SerialName("sizeId") val sizeId: String? = null,
    @SerialName("sizeName") val sizeName: String? = null
)
