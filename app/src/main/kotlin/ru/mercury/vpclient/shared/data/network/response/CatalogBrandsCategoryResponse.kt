package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogBrandsCategoryResponse(
    @SerialName("categoryId") val categoryId: Int? = null,
    @SerialName("categoryName") val categoryName: String? = null,
    @SerialName("brands") val brands: List<CatalogBrandResponse>? = null
)
