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
    val lookId: String? = null,
    val lookName: String? = null,
    val lookImageUrl: String? = null,
    val imageUrl: String,
    val imageUrls: List<String> = emptyList(),
    val isForPayment: Boolean,
    val isSold: Boolean = false,
    val isLastInStock: Boolean = false,
    val hasActions: Boolean = false,
    val isAlternativesPaletteOpen: Boolean = false,
    val isAlternativePaletteControlsAvailable: Boolean = false,
    val isSwitchAlternativeBackToOriginalAvailable: Boolean = false,
    val alternatives: List<CartProductAlternative> = emptyList(),
    val discountPercentage: Int = 0,
    val quantity: Int = 1,
    val sizeCount: Int = 1,
    val priceValue: Double = .0,
    val sizeId: String = "",
    val sizeItems: List<CartProductSize> = emptyList(),
    val isOneSize: Boolean = false,
    val dateReceipt: String? = null,
    val isDateReceiptOverdue: Boolean = false
) {
    val isSizeSelectionAvailable: Boolean
        get() = !isOneSize &&
            !sizeId.equals(NS_SIZE_ID, ignoreCase = true) &&
            sizeItems.none { size -> size.id.equals(NS_SIZE_ID, ignoreCase = true) }

    val isAlternativeSelected: Boolean
        get() = alternatives.any { alternative ->
            alternative.isOriginal && alternative.detailId != detailId
        }
}

private const val NS_SIZE_ID = "NS"
