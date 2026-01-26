package ru.mercury.vpclient.core.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("data") val data: T?,
    @SerialName("error") val error: ErrorResponse?,
    @SerialName("errors") val errors: Map<String, List<String>>?,
    @SerialName("type") val type: String?,
    @SerialName("title") val title: String?,
    @SerialName("status") val status: Int?,
    @SerialName("traceId") val traceId: String?
)
