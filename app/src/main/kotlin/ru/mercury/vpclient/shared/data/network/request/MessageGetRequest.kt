package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageGetRequest(
    @SerialName("pairedUserId") val pairedUserId: String? = null,
    @SerialName("fromMessageId") val fromMessageId: Long? = null,
    @SerialName("limit") val limit: Int? = null,
    @SerialName("toBackward") val toBackward: Boolean? = null
)
