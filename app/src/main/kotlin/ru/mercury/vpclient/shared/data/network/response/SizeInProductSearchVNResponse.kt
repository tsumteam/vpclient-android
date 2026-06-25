package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SizeInProductSearchVNResponse(
    @SerialName("availableStockQuantity") val availableStockQuantity: Double? = null,
    @SerialName("id") val id: String? = null,
    @SerialName("inOrder") val inOrder: Boolean? = null,
    @SerialName("inStock") val inStock: Boolean? = null,
    @SerialName("inStockShops") val inStockShops: List<String>? = null,
    @SerialName("isFavorite") val isFavorite: Boolean? = null,
    @SerialName("isLastInStock") val isLastInStock: Boolean? = null,
    @SerialName("name") val name: String? = null
)
