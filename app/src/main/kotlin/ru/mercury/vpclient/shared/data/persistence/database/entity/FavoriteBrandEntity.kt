package ru.mercury.vpclient.shared.data.persistence.database.entity

import androidx.room.Entity

@Entity(
    tableName = "FavoriteBrand",
    primaryKeys = ["pairedUserId", "categoryId", "brandId"]
)
data class FavoriteBrandEntity(
    val pairedUserId: String,
    val categoryId: Int,
    val categoryName: String,
    val brandId: Int,
    val name: String,
    val photoUrl: String?,
    val isTopBrand: Boolean,
    val isFavorite: Boolean,
    val restrictionType: String?,
    val position: Int
)
