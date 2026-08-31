package ru.mercury.vpclient.shared.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class MessengerPayloadVideo(
    val videoUrl: String?,
    val previewUrl: String?
)
