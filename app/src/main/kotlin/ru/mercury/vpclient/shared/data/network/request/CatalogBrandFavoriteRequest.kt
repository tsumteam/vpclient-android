package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogBrandFavoriteRequest(
    @SerialName("brandId") val brandId: Int,
    @SerialName("categoryId") val categoryId: Int
)
