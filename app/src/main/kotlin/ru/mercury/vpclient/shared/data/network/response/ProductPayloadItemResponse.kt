package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductPayloadItemResponse(
    @SerialName("brand") val brand: String? = null,
    @SerialName("colorId") val colorId: String? = null,
    @SerialName("colorName") val colorName: String? = null,
    @SerialName("id") val id: String? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("itemId") val itemId: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("price") val price: Double? = null
)
