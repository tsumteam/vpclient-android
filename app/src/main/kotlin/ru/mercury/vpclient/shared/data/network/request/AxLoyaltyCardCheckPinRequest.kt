package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AxLoyaltyCardCheckPinRequest(
    @SerialName("basketId") val basketId: String? = null,
    @SerialName("loyaltyCardNumber") val loyaltyCardNumber: String? = null,
    @SerialName("token") val token: String? = null,
    @SerialName("smsCode") val smsCode: String? = null,
    @SerialName("docType") val docType: Int? = null
)
