package ru.mercury.vpclient.shared.data.entity

data class CartProduct(
    val id: String,
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
    val oldPrice: String? = null,
    val imageUrl: String,
    val imageUrls: List<String> = emptyList(),
    val isForPayment: Boolean,
    val isSold: Boolean = false,
    val isLastInStock: Boolean = false,
    val hasActions: Boolean = false,
    val isAlternativesPaletteOpen: Boolean = false,
    val alternatives: List<CartProductAlternative> = emptyList(),
    val discountPercentage: Int = 0,
    val quantity: Int = 1,
    val sizeCount: Int = 1,
    val priceValue: Double = .0
)
