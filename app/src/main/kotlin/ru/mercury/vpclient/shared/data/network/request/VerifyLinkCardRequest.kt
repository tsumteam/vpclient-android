package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyLinkCardRequest(
    @SerialName("loyaltyCardNumber") val loyaltyCardNumber: String? = null,
    @SerialName("smsCode") val smsCode: String? = null
)
