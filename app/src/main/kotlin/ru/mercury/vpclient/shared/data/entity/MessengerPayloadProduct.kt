package ru.mercury.vpclient.shared.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class MessengerPayloadProduct(
    val id: String,
    val brand: String,
    val name: String,
    val itemId: String,
    val price: Double,
    val colorId: String,
    val colorName: String,
    val imageUrl: String?
)
