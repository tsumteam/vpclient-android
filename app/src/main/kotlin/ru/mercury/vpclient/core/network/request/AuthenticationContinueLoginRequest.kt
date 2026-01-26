package ru.mercury.vpclient.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationContinueLoginRequest(
    @SerialName("phone") val phone: String,
    @SerialName("code") val code: String
)
