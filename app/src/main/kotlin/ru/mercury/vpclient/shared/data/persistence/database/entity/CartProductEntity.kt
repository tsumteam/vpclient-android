package ru.mercury.vpclient.shared.data.persistence.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative

@Entity(tableName = "CartProduct")
data class CartProductEntity(
    @PrimaryKey val id: String,
    val position: Int,
    val detailId: String,
    val itemId: String,
    val colorId: String,
    val brand: String,
    val urlBrandLogo: String?,
    val name: String,
    val article: String,
    val color: String,
    val size: String,
    val price: String,
    val oldPrice: String?,
    val lookId: String?,
    val lookName: String?,
    val lookImageUrl: String?,
    val imageUrl: String,
    val imageUrls: List<String>,
    val isForPayment: Boolean,
    val isSold: Boolean,
    val isLastInStock: Boolean,
    val hasActions: Boolean,
    val isAlternativesPaletteOpen: Boolean,
    val isAlternativePaletteControlsAvailable: Boolean,
    val isSwitchAlternativeBackToOriginalAvailable: Boolean,
    val alternatives: List<CartProductAlternative>,
    val discountPercentage: Int,
    val quantity: Int,
    val sizeCount: Int,
    val priceValue: Double
)
