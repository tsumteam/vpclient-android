package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvailableLocationsResponseItemResponse(
    @SerialName("locationId") val locationId: String? = null,
    @SerialName("locationName") val locationName: String? = null,
    @SerialName("isOnlyInVipSite") val isOnlyInVipSite: Boolean? = null,
    @SerialName("isOnlyInTransit") val isOnlyInTransit: Boolean? = null
)
