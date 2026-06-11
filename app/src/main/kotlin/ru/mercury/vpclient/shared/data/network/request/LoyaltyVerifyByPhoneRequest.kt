package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoyaltyVerifyByPhoneRequest(
    @SerialName("phone") val phone: String,
    @SerialName("code") val code: String
)
