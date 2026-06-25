package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.PayloadType

@Serializable
data class CompilationLookPayloadResponse(
    @SerialName("compilationLooks") val compilationLooks: List<CompilationLookPayloadItemResponse> = emptyList(),
    @SerialName("text") val text: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("citation") val citation: String? = null,
    @SerialName("citatedMessageId") val citatedMessageId: Int? = null,
    @SerialName("type") val type: PayloadType? = null
)
