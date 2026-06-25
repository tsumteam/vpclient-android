package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BasketForCheckoutResponse(
    @SerialName("basketResponseDto") val basketResponseDto: BasketResponse? = null,
    @SerialName("availableBonusSum") val availableBonusSum: Double? = null,
    @SerialName("loyaltyCardNumber") val loyaltyCardNumber: String? = null,
    @SerialName("totalAvailableBonuses") val totalAvailableBonuses: Double? = null
)
