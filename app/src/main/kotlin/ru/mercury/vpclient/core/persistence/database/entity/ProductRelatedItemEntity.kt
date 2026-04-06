package ru.mercury.vpclient.core.persistence.database.entity

import kotlinx.serialization.Serializable

// fixme

@Serializable
data class ProductRelatedItemEntity(
    val id: String,
    val itemId: String,
    val colorId: String,
    val name: String?,
    val brand: String?,
    val urlBrandLogo: String?,
    val price: Double,
    val priceWithoutDiscount: Double?,
    val imageUrl: String?,
    val imageUrls: List<String>
)
