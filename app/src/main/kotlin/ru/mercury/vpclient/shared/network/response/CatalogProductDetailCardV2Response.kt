package ru.mercury.vpclient.shared.network.response

import kotlinx.serialization.Serializable

@Serializable
data class CatalogProductDetailCardV2Response(
    val name: String? = null,
    val itemId: String? = null,
    val categoryId: Int? = null,
    val brandId: Int? = null,
    val brand: String? = null,
    val urlBrandLogo: String? = null,
    val article: String? = null,
    val longDescription: String? = null,
    val productionStructure: String? = null,
    val country: String? = null,
    val shortDescription: String? = null,
    val technicalDescription: String? = null,
    val ekttId: String? = null,
    val breadcrumbs: List<String>? = null,
    val colors: List<CatalogProductDetailCardV2ColorResponse>? = null
)
