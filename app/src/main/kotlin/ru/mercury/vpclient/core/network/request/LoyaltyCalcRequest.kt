package ru.mercury.vpclient.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.core.Amount

@Serializable
data class LoyaltyCalcRequest(
    @SerialName("basketId") val basketId: String,
    @SerialName("loyaltyCardNumber") val loyaltyCardNumber: String,
    @SerialName("token") val token: String,
    @SerialName("bonusAmount") val bonusAmount: Amount
)
