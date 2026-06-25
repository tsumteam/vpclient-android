package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageReadGetResponse(
    @SerialName("pairedUserId") val pairedUserId: String? = null,
    @SerialName("revision") val revision: String? = null
)
