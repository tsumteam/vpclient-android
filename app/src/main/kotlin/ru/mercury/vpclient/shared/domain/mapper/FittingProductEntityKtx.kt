package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.persistence.database.entity.FittingProductEntity

fun CartProduct.fittingEntity(
    deliveryId: String,
    deliveryPosition: Int,
    position: Int
): FittingProductEntity {
    return FittingProductEntity(
        id = id,
        deliveryId = deliveryId,
        deliveryPosition = deliveryPosition,
        position = position,
        detailId = detailId,
        itemId = itemId,
        colorId = colorId,
        brand = brand,
        urlBrandLogo = urlBrandLogo,
        name = name,
        article = article,
        color = color,
        size = size,
        price = price,
        oldPrice = oldPrice,
        lookId = lookId,
        lookName = lookName,
        lookImageUrl = lookImageUrl,
        imageUrl = imageUrl,
        imageUrls = imageUrls,
        isForPayment = isForPayment,
        isSold = isSold,
        isLastInStock = isLastInStock,
        hasActions = hasActions,
        isAlternativesPaletteOpen = isAlternativesPaletteOpen,
        isAlternativePaletteControlsAvailable = isAlternativePaletteControlsAvailable,
        isSwitchAlternativeBackToOriginalAvailable = isSwitchAlternativeBackToOriginalAvailable,
        alternatives = alternatives,
        discountPercentage = discountPercentage,
        quantity = quantity,
        sizeCount = sizeCount,
        priceValue = priceValue,
        sizeId = sizeId,
        sizeItems = sizeItems,
        dateReceipt = dateReceipt,
        isDateReceiptOverdue = isDateReceiptOverdue
    )
}

val FittingProductEntity.cartProduct: CartProduct
    get() = CartProduct(
        id = id,
        detailId = detailId,
        itemId = itemId,
        colorId = colorId,
        brand = brand,
        urlBrandLogo = urlBrandLogo,
        name = name,
        article = article,
        color = color,
        size = size,
        price = price,
        oldPrice = oldPrice,
        lookId = lookId,
        lookName = lookName,
        lookImageUrl = lookImageUrl,
        imageUrl = imageUrl,
        imageUrls = imageUrls,
        isForPayment = isForPayment,
        isSold = isSold,
        isLastInStock = isLastInStock,
        hasActions = hasActions,
        isAlternativesPaletteOpen = isAlternativesPaletteOpen,
        isAlternativePaletteControlsAvailable = isAlternativePaletteControlsAvailable,
        isSwitchAlternativeBackToOriginalAvailable = isSwitchAlternativeBackToOriginalAvailable,
        alternatives = alternatives,
        discountPercentage = discountPercentage,
        quantity = quantity,
        sizeCount = sizeCount,
        priceValue = priceValue,
        sizeId = sizeId,
        sizeItems = sizeItems,
        dateReceipt = dateReceipt,
        isDateReceiptOverdue = isDateReceiptOverdue
    )
