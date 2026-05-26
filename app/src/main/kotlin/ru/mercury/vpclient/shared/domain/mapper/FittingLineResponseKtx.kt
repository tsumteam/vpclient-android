package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.FORMAT_RUB
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.FittingDeliveryHeader
import ru.mercury.vpclient.shared.data.network.entity.FittingDeliveryResponseDto
import ru.mercury.vpclient.shared.data.network.entity.FittingLineResponseDto
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

val FittingLineResponseDto.cartProduct: CartProduct?
    get() {
        val product = product ?: return null
        val id = lineId ?: product.id ?: product.itemId ?: product.article ?: return null
        val detailId = product.id ?: product.itemId ?: id
        val price = product.price ?: product.currentRetailPrice ?: .0
        val oldPrice = product.priceWithoutDiscount
        val sizes = product.sizes.orEmpty()
        val article = product.article?.takeIf { it.isNotEmpty() } ?: product.itemId.orEmpty()
        val imageUrls = product.imageUrls.orEmpty().filter { it.isNotEmpty() }
        val rawImageUrl = product.imageUrl.orEmpty()
        val imageUrl = when {
            rawImageUrl.isNotEmpty() -> rawImageUrl
            else -> imageUrls.firstOrNull().orEmpty()
        }

        return CartProduct(
            id = id,
            detailId = detailId,
            itemId = product.itemId.orEmpty(),
            colorId = product.colorId.orEmpty(),
            brand = product.brand.orEmpty(),
            urlBrandLogo = product.urlBrandLogo,
            name = product.name.orEmpty(),
            article = article,
            color = product.colorName.orEmpty(),
            size = sizes.joinToString(", ") { it.name.orEmpty() },
            price = price.formattedFittingPrice,
            oldPrice = oldPrice.formattedOldFittingPrice(price),
            imageUrl = imageUrl,
            imageUrls = imageUrls,
            isForPayment = paySwitch ?: product.paySwitch ?: false,
            isSold = logisticStatusRejectReason != null,
            isLastInStock = sizes.any { it.isLastInStock == true || it.availableStockQuantity == 1.0 },
            hasActions = product.actions.orEmpty().isNotEmpty(),
            discountPercentage = product.discountPercentage ?: 0,
            quantity = 1,
            sizeCount = sizes.size.coerceAtLeast(1),
            priceValue = price
        )
    }

val FittingDeliveryResponseDto.fittingDeliveryHeader: FittingDeliveryHeader
    get() {
        return FittingDeliveryHeader(
            status = deliveryStatusAsString.orEmpty(),
            date = deliveryDateAsString ?: "Время доставки не указано",
            address = address ?: "Адрес доставки не указан",
            isDelivered = deliveryStatusAsString?.lowercase() == "доставлено"
        )
    }

private val Double.formattedFittingPrice: String
    get() {
        return FORMAT_RUB.format(fittingPriceFormatter.format(this))
    }

private fun Double?.formattedOldFittingPrice(currentPrice: Double): String? {
    return when {
        this == null -> null
        this <= currentPrice -> null
        else -> FORMAT_RUB.format(fittingPriceFormatter.format(this))
    }
}

private val fittingPriceFormatter = DecimalFormat(
    "#,###",
    DecimalFormatSymbols(Locale.Builder().setLanguage("ru").setRegion("RU").build()).apply {
        groupingSeparator = ' '
    }
)
