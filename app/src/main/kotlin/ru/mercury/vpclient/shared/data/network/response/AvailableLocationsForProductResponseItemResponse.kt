package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvailableLocationsForProductResponseItemResponse(
    @SerialName("locationId") val locationId: String? = null,
    @SerialName("locationName") val locationName: String? = null
)
