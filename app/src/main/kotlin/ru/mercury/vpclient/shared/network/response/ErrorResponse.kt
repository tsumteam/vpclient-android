package ru.mercury.vpclient.shared.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    @SerialName("code") val code: Int?,
    @SerialName("display") val display: String?,
    @SerialName("msg") val msg: String?,
    @SerialName("reason") val reason: String?
)
