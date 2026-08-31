package ru.mercury.vpclient.shared.data.network.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.mercury.vpclient.shared.data.network.type.PayloadType

@Serializable
data class PayloadDtoBaseResponse(
    @SerialName("text") val text: String? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("citation") val citation: String? = null,
    @SerialName("citatedMessageId") val citatedMessageId: Int? = null,
    @SerialName("type") val type: PayloadType? = null,
    @SerialName("orderNumber") val orderNumber: String? = null,
    @SerialName("products") val products: List<ProductPayloadItemResponse>? = null,
    @SerialName("images") val images: List<ImagesPayloadItemResponse>? = null,
    @SerialName("videos") val videos: List<VideosPayloadItemResponse>? = null,
    @SerialName("compilationLooks") val compilationLooks: List<CompilationLookPayloadItemResponse>? = null,
    @SerialName("clientCompilations") val clientCompilations: List<ClientCompilationPayloadItemResponse>? = null,
    @SerialName("basketLooks") val basketLooks: List<BasketLookPayloadItemResponse>? = null
)
