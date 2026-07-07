package ru.mercury.vpclient.shared.domain.mapper

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import ru.mercury.vpclient.shared.data.FORMAT_RUB
import ru.mercury.vpclient.shared.data.network.request.BasketOperationRequest
import ru.mercury.vpclient.shared.data.network.response.BasketAddLineOperationRequestItemResponse
import ru.mercury.vpclient.shared.data.network.type.BasketOperationRequestType
import ru.mercury.vpclient.shared.data.persistence.database.entity.CartProductEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import java.util.UUID
import kotlin.math.roundToInt

private val CatalogFilterProductsEntity.cardImageUrls: List<String>
    get() = when {
        imageUrls.isNotEmpty() -> imageUrls
        imageUrl.isNotEmpty() -> listOfNotNull(imageUrl)
        else -> emptyList()
    }

val CatalogFilterProductsEntity.isDiscountPriceVisible: Boolean
    get() = price != 0.0 && cardOldPrice != null && cardDiscountedPrice != null

val CatalogFilterProductsEntity.isDiscountLabelVisible: Boolean
    get() = price != 0.0 && cardDiscountLabel != null

val CatalogFilterProductsEntity.imagePages: List<String>
    get() = cardImageUrls.ifEmpty { listOf("") }

fun CatalogFilterProductsEntity.addProductToBasketRequest(
    pairedUserId: String,
    sizeId: String?,
    lineId: String = UUID.randomUUID().toString()
): BasketOperationRequest {
    return BasketOperationRequest(
        pairedUserId = pairedUserId,
        items = listOf(
            catalogFilterProductJson.encodeToJsonElement(
                BasketAddLineOperationRequestItemResponse(
                    operationType = BasketOperationRequestType.ADD_LINE,
                    operationOrder = 0,
                    colorId = colorId,
                    itemId = itemId,
                    lineId = lineId,
                    sizeId = sizeId
                )
            )
        )
    )
}

fun CatalogFilterProductsEntity.optimisticCartProductEntity(position: Int, lineId: String): CartProductEntity {
    return CartProductEntity(
        id = lineId,
        position = position,
        detailId = id,
        itemId = itemId,
        colorId = colorId,
        brand = brand,
        urlBrandLogo = urlBrandLogo,
        name = name,
        article = itemId,
        color = "",
        size = "",
        price = cardPrice,
        oldPrice = cardOldPrice,
        lookId = null,
        lookName = null,
        lookImageUrl = null,
        imageUrl = imageUrl,
        imageUrls = imageUrls,
        isForPayment = true,
        isSold = false,
        isLastInStock = false,
        hasActions = actionLabels.isNotEmpty(),
        isAlternativesPaletteOpen = false,
        isAlternativePaletteControlsAvailable = false,
        isSwitchAlternativeBackToOriginalAvailable = false,
        alternatives = emptyList(),
        discountPercentage = 0,
        quantity = 1,
        sizeCount = 1,
        priceValue = price,
        sizeId = "",
        sizeItems = emptyList()
    )
}

fun CatalogFilterProductsEntity.messageSheetProductEntity(): ProductEntity {
    return ProductEntity.Empty.copy(
        id = id,
        name = name,
        itemId = itemId,
        brand = brand,
        colorName = colorId,
        urlBrandLogo = urlBrandLogo,
        shortDescription = name,
        price = price,
        priceWithoutDiscount = priceWithoutDiscount,
        colorImageUrls = imagePages
    )
}

private val catalogFilterProductJson = Json {
    explicitNulls = false
}

val CatalogFilterProductsEntity.cardPrice: String
    get() = price.formattedPrice()

val CatalogFilterProductsEntity.cardOldPrice: String?
    get() = priceWithoutDiscount.formattedOldPrice(price)

val CatalogFilterProductsEntity.cardDiscountedPrice: String?
    get() = price.formattedDiscountedPrice(priceWithoutDiscount)

val CatalogFilterProductsEntity.cardDiscountLabel: String?
    get() = price.discountLabel(priceWithoutDiscount)

val CatalogFilterProductsEntity.fullPrice: Double
    get() = priceWithoutDiscount ?: price

val CatalogFilterProductsEntity.fullPriceText: String
    get() = fullPrice.roundToInt().formatPriceText()

val CatalogFilterProductsEntity.priceText: String
    get() = price.roundToInt().formatPriceText()

val CatalogFilterProductsEntity.isDiscountVisible: Boolean
    get() = fullPrice > price

val CatalogFilterProductsEntity.discountPercentText: String
    get() {
        val percent = ((fullPrice - price) / fullPrice * 100).roundToInt()
        return "-$percent%"
    }

val CatalogFilterProductsEntity.compilationBenefitFullPrice: Double
    get() = lookActionPriceWithoutDiscount ?: price

val CatalogFilterProductsEntity.compilationBenefitDiscountPrice: Double
    get() = lookActionPrice ?: price

val CatalogFilterProductsEntity.compilationBenefitFullPriceText: String
    get() = compilationBenefitFullPrice.roundToInt().formatPriceText()

val CatalogFilterProductsEntity.compilationBenefitDiscountPriceText: String
    get() = compilationBenefitDiscountPrice.roundToInt().formatPriceText()

val CatalogFilterProductsEntity.isCompilationBenefitDiscountVisible: Boolean
    get() = compilationBenefitFullPrice > compilationBenefitDiscountPrice

val CatalogFilterProductsEntity.compilationBenefitDiscountPercentText: String
    get() {
        val percent = lookActionDiscountPercentage ?: when {
            compilationBenefitFullPrice == 0.0 -> 0
            else -> ((compilationBenefitFullPrice - compilationBenefitDiscountPrice) / compilationBenefitFullPrice * 100).roundToInt()
        }
        return "-$percent%"
    }

fun Int.formatPriceText(): String {
    return "%,d ₽".format(this).replace(',', ' ')
}

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
        currentPrice == 0.0 -> null
        this <= currentPrice -> null
        else -> formatPrice(this)
    }
}

private fun Double?.formattedDiscountedPrice(oldPrice: Double?): String? {
    return when {
        this == null -> null
        oldPrice == null -> null
        this == 0.0 -> null
        oldPrice <= this -> null
        else -> formatPrice(this)
    }
}

private fun Double?.discountLabel(oldPrice: Double?): String? {
    return when {
        this == null -> null
        oldPrice == null -> null
        this == 0.0 -> null
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
    DecimalFormatSymbols(Locale.Builder().setLanguage("ru").setRegion("RU").build()).apply {
        groupingSeparator = ' '
    }
)
