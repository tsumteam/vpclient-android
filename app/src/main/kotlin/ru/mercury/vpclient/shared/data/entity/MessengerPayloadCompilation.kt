package ru.mercury.vpclient.shared.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class MessengerPayloadCompilation(
    val compilationId: Int,
    val compilationName: String,
    val compilationDescription: String?,
    val imageUrl: String?
)
