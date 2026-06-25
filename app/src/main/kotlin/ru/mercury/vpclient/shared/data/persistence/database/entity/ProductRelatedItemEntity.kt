package ru.mercury.vpclient.shared.data.persistence.database.entity

import kotlinx.serialization.Serializable

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
    val imageUrls: List<String>,
    val additionalColorPhotoUrls: List<String> = emptyList(),
    val actionLabels: List<String> = emptyList()
)
