package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogBrandResponse(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("photoUrl") val photoUrl: String? = null,
    @SerialName("isTopBrand") val isTopBrand: Boolean? = null,
    @SerialName("isFavorite") val isFavorite: Boolean? = null,
    @SerialName("restrictionType") val restrictionType: String? = null
)
