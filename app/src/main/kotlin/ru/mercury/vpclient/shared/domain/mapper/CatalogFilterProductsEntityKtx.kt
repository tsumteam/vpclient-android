package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.FORMAT_RUB
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.roundToInt

private val CatalogFilterProductsEntity.cardImageUrls: List<String>
    get() = when {
        imageUrls.isNotEmpty() -> imageUrls
        !imageUrl.isNullOrEmpty() -> listOfNotNull(imageUrl)
        else -> emptyList()
    }

val CatalogFilterProductsEntity.isDiscountPriceVisible: Boolean
    get() = cardOldPrice != null && cardDiscountedPrice != null

val CatalogFilterProductsEntity.isDiscountLabelVisible: Boolean
    get() = cardDiscountLabel != null

val CatalogFilterProductsEntity.imagePages: List<String>
    get() = cardImageUrls.ifEmpty { listOf("") }



// fixme

val CatalogFilterProductsEntity.cardPrice: String
    get() = price.formattedPrice()

val CatalogFilterProductsEntity.cardOldPrice: String?
    get() = priceWithoutDiscount.formattedOldPrice(price)

val CatalogFilterProductsEntity.cardDiscountedPrice: String?
    get() = price.formattedDiscountedPrice(priceWithoutDiscount)

val CatalogFilterProductsEntity.cardDiscountLabel: String?
    get() = price.discountLabel(priceWithoutDiscount)

private fun Double?.formattedPrice(): String {
    return when {
        this == null -> ""
        else -> formatPrice(this)
    }
}

private fun Double?.formattedOldPrice(currentPrice: Double?): String? {
    return when {
        this == null -> null
        currentPrice == null -> null
        this <= currentPrice -> null
        else -> formatPrice(this)
    }
}

private fun Double?.formattedDiscountedPrice(oldPrice: Double?): String? {
    return when {
        this == null -> null
        oldPrice == null -> null
        oldPrice <= this -> null
        else -> formatPrice(this)
    }
}

private fun Double?.discountLabel(oldPrice: Double?): String? {
    return when {
        this == null -> null
        oldPrice == null -> null
        oldPrice <= this -> null
        oldPrice == 0.0 -> null
        else -> {
            val discountPercent = ((oldPrice - this) / oldPrice * 100).roundToInt()
            "-$discountPercent%"
        }
    }
}

private fun formatPrice(price: Double): String {
    return FORMAT_RUB.format(decimalPriceFormatter.format(price))
}

private val decimalPriceFormatter = DecimalFormat(
    "#,###",
    DecimalFormatSymbols(Locale("ru", "RU")).apply {
        groupingSeparator = ' '
    }
)
