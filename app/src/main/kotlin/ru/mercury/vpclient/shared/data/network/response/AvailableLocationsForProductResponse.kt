package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvailableLocationsForProductResponse(
    @SerialName("barcode") val barcode: String? = null,
    @SerialName("items") val items: List<AvailableLocationsForProductResponseItemResponse>? = null
)
