package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.FORMAT_RUB
import ru.mercury.vpclient.shared.data.network.entity.BasketLineResponseDto
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

val BasketLineResponseDto.cartProduct: CartProduct?
    get() {
        val basketProduct = products.orEmpty().firstOrNull()
        val product = basketProduct?.product
        val id = lineId ?: basketProduct?.productId ?: product?.id ?: product?.itemId ?: product?.article ?: return null
        val detailId = basketProduct?.productId ?: product?.id ?: product?.itemId ?: id
        val price = product?.price ?: product?.currentRetailPrice ?: .0
        val oldPrice = product?.priceWithoutDiscount
        val sizes = product?.sizes.orEmpty()
        val article = product?.article?.takeIf { it.isNotEmpty() } ?: product?.itemId.orEmpty()
        val imageUrls = product?.imageUrls.orEmpty().filter { it.isNotEmpty() }
        val rawImageUrl = product?.imageUrl.orEmpty()
        val imageUrl = when {
            rawImageUrl.isNotEmpty() -> rawImageUrl
            else -> imageUrls.firstOrNull().orEmpty()
        }

        return CartProduct(
            id = id,
            detailId = detailId,
            itemId = product?.itemId.orEmpty(),
            colorId = product?.colorId.orEmpty(),
            brand = product?.brand.orEmpty(),
            urlBrandLogo = product?.urlBrandLogo,
            name = product?.name.orEmpty(),
            article = article,
            color = product?.colorName.orEmpty(),
            size = sizes.joinToString(", ") { it.name.orEmpty() },
            price = price.formattedCartPrice,
            oldPrice = oldPrice.formattedOldCartPrice(price),
            imageUrl = imageUrl,
            imageUrls = imageUrls,
            isForPayment = paySwitch ?: product?.paySwitch ?: false,
            isSold = sizes.any { it.inStock == false },
            isLastInStock = sizes.any { it.isLastInStock == true || it.availableStockQuantity == 1.0 },
            hasActions = product?.actions.orEmpty().isNotEmpty(),
            discountPercentage = product?.discountPercentage ?: 0,
            quantity = quantity ?: product?.quantity ?: 1,
            sizeCount = sizes.size.coerceAtLeast(1),
            priceValue = price
        )
    }

private val Double.formattedCartPrice: String
    get() {
        return FORMAT_RUB.format(cartPriceFormatter.format(this))
    }

private fun Double?.formattedOldCartPrice(currentPrice: Double): String? {
    return when {
        this == null -> null
        this <= currentPrice -> null
        else -> FORMAT_RUB.format(cartPriceFormatter.format(this))
    }
}

private val cartPriceFormatter = DecimalFormat(
    "#,###",
    DecimalFormatSymbols(Locale.Builder().setLanguage("ru").setRegion("RU").build()).apply {
        groupingSeparator = ' '
    }
)
