package ru.mercury.vpclient.shared.data.network.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ContinueLoginRequest(
    @SerialName("phone") val phone: String? = null,
    @SerialName("code") val code: String? = null
)
