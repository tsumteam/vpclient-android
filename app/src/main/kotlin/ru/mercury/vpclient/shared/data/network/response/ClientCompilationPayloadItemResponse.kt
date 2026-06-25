package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ClientCompilationPayloadItemResponse(
    @SerialName("compilationId") val compilationId: Int? = null,
    @SerialName("compilationName") val compilationName: String? = null,
    @SerialName("compilationDescription") val compilationDescription: String? = null,
    @SerialName("imageUrl") val imageUrl: String? = null
)
