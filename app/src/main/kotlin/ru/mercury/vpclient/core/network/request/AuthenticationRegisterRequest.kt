package ru.mercury.vpclient.core.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationRegisterRequest(
    @SerialName("phone") val phone: String,
    @SerialName("name") val name: String
)
