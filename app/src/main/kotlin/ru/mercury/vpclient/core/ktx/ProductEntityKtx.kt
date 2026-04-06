package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.FORMAT_RUB
import ru.mercury.vpclient.core.persistence.database.entity.ProductEntity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.roundToInt

// fixme

val ProductEntity.isDiscountPriceVisible: Boolean
    get() = cardOldPrice != null && cardDiscountedPrice != null

val ProductEntity.isDiscountLabelVisible: Boolean
    get() = cardDiscountLabel != null

val ProductEntity.cardPrice: String
    get() = price.formattedPrice()

val ProductEntity.cardOldPrice: String?
    get() = priceWithoutDiscount.formattedOldPrice(price)

val ProductEntity.cardDiscountedPrice: String?
    get() = price.formattedDiscountedPrice(priceWithoutDiscount)

val ProductEntity.cardDiscountLabel: String?
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
    return FORMAT_RUB.format(decimalProductPriceFormatter.format(price))
}

private val decimalProductPriceFormatter = DecimalFormat(
    "#,###",
    DecimalFormatSymbols(Locale("ru", "RU")).apply {
        groupingSeparator = ' '
    }
)
