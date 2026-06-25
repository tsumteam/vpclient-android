package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AvailableColorsResponseItemResponse(
    @SerialName("colorFullName") val colorFullName: String? = null,
    @SerialName("colorHex") val colorHex: String? = null,
    @SerialName("colorId") val colorId: String? = null,
    @SerialName("isOnlyInVipSite") val isOnlyInVipSite: Boolean? = null,
    @SerialName("isOnlyInTransit") val isOnlyInTransit: Boolean? = null
)
