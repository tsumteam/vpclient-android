package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogFashionImageCardResponse(
    @SerialName("id") val id: String? = null,
    @SerialName("imageUrl") val imageUrl: String? = null,
    @SerialName("items") val items: List<CatalogProductSearchCardResponse>? = null
)
