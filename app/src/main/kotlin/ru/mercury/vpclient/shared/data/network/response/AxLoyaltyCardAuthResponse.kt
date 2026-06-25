package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AxLoyaltyCardAuthResponse(
    @SerialName("token") val token: String? = null,
    @SerialName("identityIsRequired") val identityIsRequired: Boolean? = null,
    @SerialName("showPinField") val showPinField: Boolean? = null
)
