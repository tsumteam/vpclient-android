package ru.mercury.vpclient.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoyaltyRollbackRequest(
    @SerialName("basketId") val basketId: String,
    @SerialName("loyaltyCardNumber") val loyaltyCardNumber: String,
    @SerialName("token") val token: String
)
