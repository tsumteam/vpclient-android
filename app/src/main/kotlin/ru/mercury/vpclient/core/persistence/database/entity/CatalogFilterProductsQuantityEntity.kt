package ru.mercury.vpclient.core.persistence.database.entity

import androidx.room.Entity

@Entity(
    tableName = "CatalogFilterProductsQuantity",
    primaryKeys = ["categoryId", "titleCategoryId"]
)
data class CatalogFilterProductsQuantityEntity(
    val categoryId: Int,
    val titleCategoryId: Int,
    val productsQuantity: Int?
) {
    companion object {
        val Empty = CatalogFilterProductsQuantityEntity(
            categoryId = 0,
            titleCategoryId = 0,
            productsQuantity = null
        )
    }
}
