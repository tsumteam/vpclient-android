package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientActivityInfoResponse(
    @SerialName("clientId") val clientId: String? = null,
    @SerialName("activationDate") val activationDate: String? = null,
    @SerialName("lastActivity") val lastActivity: String? = null
)
