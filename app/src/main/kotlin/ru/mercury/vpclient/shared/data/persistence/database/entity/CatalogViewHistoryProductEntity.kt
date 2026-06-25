package ru.mercury.vpclient.shared.data.persistence.database.entity

import androidx.room.Entity

@Entity(
    tableName = "CatalogViewHistoryProducts",
    primaryKeys = ["id"]
)
data class CatalogViewHistoryProductEntity(
    val id: String,
    val itemId: String,
    val colorId: String,
    val name: String,
    val price: Double,
    val priceWithoutDiscount: Double?,
    val brand: String,
    val urlBrandLogo: String?,
    val imageUrl: String,
    val imageUrls: List<String>,
    val additionalColorPhotoUrls: List<String>,
    val position: Int,
    val actionLabels: List<String>
)
