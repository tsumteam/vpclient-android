package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MotivationRequest(
    @SerialName("hrId") val hrId: Int? = null,
    @SerialName("date") val date: String? = null
)
