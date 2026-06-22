package ru.mercury.vpclient.shared.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class CartProductSize(
    val id: String,
    val name: String,
    val productId: String,
    val catalogProductId: String = "",
    val isLastInStock: Boolean = false
)
