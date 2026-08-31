package ru.mercury.vpclient.shared.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class MessengerPayloadLook(
    val id: String,
    val name: String,
    val imageUrl: String?,
    val compilationName: String? = null
)
