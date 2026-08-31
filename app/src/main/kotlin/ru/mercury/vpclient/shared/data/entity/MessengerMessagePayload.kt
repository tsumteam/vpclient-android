package ru.mercury.vpclient.shared.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class MessengerMessagePayload(
    val type: MessengerMessagePayloadType? = null,
    val title: String? = null,
    val citation: String? = null,
    val citatedText: String? = null,
    val orderNumber: String? = null,
    val products: List<MessengerPayloadProduct> = emptyList(),
    val images: List<MessengerPayloadImage> = emptyList(),
    val videos: List<MessengerPayloadVideo> = emptyList(),
    val compilationLooks: List<MessengerPayloadLook> = emptyList(),
    val clientCompilations: List<MessengerPayloadCompilation> = emptyList(),
    val basketLooks: List<MessengerPayloadLook> = emptyList()
)
