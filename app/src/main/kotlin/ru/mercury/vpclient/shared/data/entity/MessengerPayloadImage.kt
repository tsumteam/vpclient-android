package ru.mercury.vpclient.shared.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class MessengerPayloadImage(
    val imageUrl: String?,
    val previewUrl: String?
)
