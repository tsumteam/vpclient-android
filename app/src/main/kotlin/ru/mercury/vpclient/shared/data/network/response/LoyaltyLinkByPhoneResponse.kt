package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoyaltyLinkByPhoneResponse(
    @SerialName("isNeedVerification") val isNeedVerification: Boolean? = null,
    @SerialName("error") val error: ErrorResponse? = null
)
