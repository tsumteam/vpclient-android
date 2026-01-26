package ru.mercury.vpclient.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    @SerialName("username") val username: String,
    @SerialName("password") val password: String,
    @SerialName("deviceId") val deviceId: String
)
