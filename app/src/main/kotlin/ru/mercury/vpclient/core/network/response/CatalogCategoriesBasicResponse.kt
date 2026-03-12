package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.Serializable

@Serializable
data class CatalogCategoriesBasicResponse(
    val items: List<CatalogCategoryResponse>?
)
