package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogBrandsResponse(
    @SerialName("items") val items: List<CatalogBrandCategoryResponse>
)

@Serializable
data class CatalogBrandCategoryResponse(
    @SerialName("categoryId") val categoryId: Long,
    @SerialName("categoryName") val categoryName: String,
    @SerialName("brands") val brands: List<CatalogBrandResponse>
)

@Serializable
data class CatalogBrandResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("photoUrl") val photoUrl: String?,
    @SerialName("isTopBrand") val isTopBrand: Boolean
)
