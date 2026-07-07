package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvailableMultipleSizesResponse(
    @SerialName("items") val items: List<AvailableSizesResponseItemResponse>? = null,
    @SerialName("itemId") val itemId: String? = null,
    @SerialName("colorId") val colorId: String? = null,
    @SerialName("oneSize") val oneSize: Boolean? = null,
    @SerialName("countryCode") val countryCode: String? = null,
    @SerialName("sizeTableTitle") val sizeTableTitle: String? = null,
    @SerialName("sizeTableUrl") val sizeTableUrl: String? = null
)
