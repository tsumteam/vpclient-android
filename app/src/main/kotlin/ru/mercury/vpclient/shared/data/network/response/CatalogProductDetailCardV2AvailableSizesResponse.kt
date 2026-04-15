package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.Serializable

@Serializable
data class CatalogProductDetailCardV2AvailableSizesResponse(
    val items: List<CatalogProductDetailCardV2AvailableSizeResponse>? = null,
    val countryCode: String? = null,
    val sizeTableTitle: String? = null,
    val sizeTableUrl: String? = null
)
