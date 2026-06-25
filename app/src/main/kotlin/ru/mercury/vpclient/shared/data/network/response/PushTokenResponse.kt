package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PushTokenResponse(
    @SerialName("clientId") val clientId: String? = null,
    @SerialName("employeeId") val employeeId: String? = null,
    @SerialName("token") val token: String? = null
)
