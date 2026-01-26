package ru.mercury.vpclient.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoyaltyAuthRequest(
    @SerialName("basketId") val basketId: String,
    @SerialName("loyaltyCardNumber") val loyaltyCardNumber: String
)
