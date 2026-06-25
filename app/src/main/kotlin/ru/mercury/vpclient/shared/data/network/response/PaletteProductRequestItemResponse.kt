package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaletteProductRequestItemResponse(
    @SerialName("colorId") val colorId: String? = null,
    @SerialName("itemId") val itemId: String? = null
)
