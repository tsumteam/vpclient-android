package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogProductDetailCardV2Response(
    @SerialName("name") val name: String? = null,
    @SerialName("itemId") val itemId: String? = null,
    @SerialName("categoryId") val categoryId: Int? = null,
    @SerialName("brandId") val brandId: Int? = null,
    @SerialName("brand") val brand: String? = null,
    @SerialName("urlBrandLogo") val urlBrandLogo: String? = null,
    @SerialName("article") val article: String? = null,
    @SerialName("longDescription") val longDescription: String? = null,
    @SerialName("productionStructure") val productionStructure: String? = null,
    @SerialName("country") val country: String? = null,
    @SerialName("shortDescription") val shortDescription: String? = null,
    @SerialName("technicalDescription") val technicalDescription: String? = null,
    @SerialName("ekttId") val ekttId: String? = null,
    @SerialName("breadcrumbs") val breadcrumbs: List<String>? = null,
    @SerialName("buttons") val buttons: List<CatalogProductDetailCardV2ButtonResponse>? = null,
    @SerialName("colors") val colors: List<CatalogProductDetailCardV2ColorResponse>? = null
)
