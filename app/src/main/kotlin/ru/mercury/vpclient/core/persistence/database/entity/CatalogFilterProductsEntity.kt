package ru.mercury.vpclient.core.persistence.database.entity

import androidx.room.Entity

@Entity(
    tableName = "CatalogFilterProducts",
    primaryKeys = ["categoryId", "titleCategoryId", "id"]
)
data class CatalogFilterProductsEntity(
    val categoryId: Int,
    val titleCategoryId: Int,
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
    val position: Int
) {
    companion object {
        val Empty = CatalogFilterProductsEntity(
            categoryId = 0,
            titleCategoryId = 0,
            id = "",
            itemId = "",
            colorId = "",
            name = "",
            price = .0,
            priceWithoutDiscount = .0,
            brand = "",
            urlBrandLogo = null,
            imageUrl = "",
            imageUrls = emptyList(),
            position = 0
        )
    }
}
