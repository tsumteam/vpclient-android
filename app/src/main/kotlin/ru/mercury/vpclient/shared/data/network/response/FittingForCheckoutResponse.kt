package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FittingForCheckoutResponse(
    @SerialName("fittingResponseDto") val fittingResponseDto: FittingResponse? = null,
    @SerialName("availableBonusSum") val availableBonusSum: Double? = null,
    @SerialName("loyaltyCardNumber") val loyaltyCardNumber: String? = null,
    @SerialName("totalAvailableBonuses") val totalAvailableBonuses: Double? = null
)
