package ru.mercury.vpclient.shared.data.persistence.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// fixme

@Entity(tableName = "Product")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String?,
    val itemId: String?,
    val categoryId: Int?,
    val brandId: Int?,
    val brand: String?,
    val colorName: String?,
    val urlBrandLogo: String?,
    val article: String?,
    val longDescription: String?,
    val artDescription: String?,
    val productionStructure: String?,
    val country: String?,
    val shortDescription: String?,
    val technicalDescription: String?,
    val ekttId: String?,
    val price: Double?,
    val priceWithoutDiscount: Double?,
    val breadcrumbs: List<String>,
    val buttons: List<ProductButtonEntity>,
    val colorImageUrls: List<String>,
    val otherColors: List<ProductOtherColorEntity>,
    val urlItemVideo: String?,
    val cashboxActions: List<String>,
    val availableSizes: ProductAvailableSizesEntity?,
    val hasWearWith: Boolean,
    val wearWithButtonEnabled: Boolean,
    val wearWithProducts: List<ProductRelatedItemEntity>,
    val completeSetProducts: List<ProductRelatedItemEntity>
) {
    companion object {
        val Empty = ProductEntity(
            id = "",
            name = null,
            itemId = null,
            categoryId = null,
            brandId = null,
            brand = null,
            colorName = null,
            urlBrandLogo = null,
            article = null,
            longDescription = null,
            artDescription = null,
            productionStructure = null,
            country = null,
            shortDescription = null,
            technicalDescription = null,
            ekttId = null,
            price = null,
            priceWithoutDiscount = null,
            breadcrumbs = emptyList(),
            buttons = emptyList(),
            colorImageUrls = emptyList(),
            otherColors = emptyList(),
            urlItemVideo = null,
            cashboxActions = emptyList(),
            availableSizes = null,
            hasWearWith = false,
            wearWithButtonEnabled = false,
            wearWithProducts = emptyList(),
            completeSetProducts = emptyList()
        )
    }
}
