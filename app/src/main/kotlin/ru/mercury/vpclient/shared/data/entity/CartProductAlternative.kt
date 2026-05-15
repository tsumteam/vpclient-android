package ru.mercury.vpclient.shared.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class CartProductAlternative(
    val id: String,
    val detailId: String,
    val brand: String,
    val urlBrandLogo: String?,
    val price: String,
    val imageUrl: String,
    val isOriginal: Boolean
)
