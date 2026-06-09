package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatalogProductDetailCardV2AvailableSizesResponse(
    @SerialName("items") val items: List<CatalogProductDetailCardV2AvailableSizeResponse>? = null,
    @SerialName("countryCode") val countryCode: String? = null,
    @SerialName("sizeTableTitle") val sizeTableTitle: String? = null,
    @SerialName("sizeTableUrl") val sizeTableUrl: String? = null
)
