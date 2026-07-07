package ru.mercury.vpclient.shared.data.persistence.database.entity

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
    val additionalColorPhotoUrls: List<String>,
    val position: Int,
    val actionLabels: List<String> = emptyList(),
    val lookActionPrice: Double? = null,
    val lookActionPriceWithoutDiscount: Double? = null,
    val lookActionName: String? = null,
    val lookActionDiscountPercentage: Int? = null,
    val availableSizes: ProductAvailableSizesEntity? = null,
    val isOneSize: Boolean = false
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
            additionalColorPhotoUrls = emptyList(),
            position = 0,
            actionLabels = emptyList(),
            lookActionPrice = null,
            lookActionPriceWithoutDiscount = null,
            lookActionName = null,
            lookActionDiscountPercentage = null,
            availableSizes = null,
            isOneSize = false
        )
    }
}
