package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCheckOutFlagsResponse(
    @SerialName("kittingSettings") val kittingSettings: CheckOutKittingSettingsResponse? = null,
    @SerialName("deliverySettings") val deliverySettings: CheckOutDeliverySettingsResponse? = null,
    @SerialName("controls") val controls: GetCheckOutFlagsControlResponse? = null
)
