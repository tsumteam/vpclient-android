package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvailableSizesResponse(
    @SerialName("items") val items: List<AvailableSizesResponseItemResponse>? = null,
    @SerialName("sizeTableTitle") val sizeTableTitle: String? = null,
    @SerialName("sizeTableUrl") val sizeTableUrl: String? = null
)
