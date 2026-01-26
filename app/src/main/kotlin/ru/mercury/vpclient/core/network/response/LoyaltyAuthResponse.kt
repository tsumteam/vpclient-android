package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoyaltyAuthResponse(
    @SerialName("token") val token: String?,
    @SerialName("identityIsRequired") val identityIsRequired: Boolean?,
    @SerialName("showPinField") val showPinField: Boolean?
)
