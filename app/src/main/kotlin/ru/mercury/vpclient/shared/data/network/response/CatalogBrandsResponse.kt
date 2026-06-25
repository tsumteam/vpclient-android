package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogBrandsResponse(
    @SerialName("items") val items: List<CatalogBrandsCategoryResponse>? = null
)
