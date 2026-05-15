package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.persistence.database.entity.CartProductEntity

fun CartProduct.entity(position: Int): CartProductEntity {
    return CartProductEntity(
        id = id,
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
        imageUrl = imageUrl,
        imageUrls = imageUrls,
        isForPayment = isForPayment,
        isSold = isSold,
        isLastInStock = isLastInStock,
        hasActions = hasActions,
        isAlternativesPaletteOpen = isAlternativesPaletteOpen,
        alternatives = alternatives,
        discountPercentage = discountPercentage,
        quantity = quantity,
        sizeCount = sizeCount,
        priceValue = priceValue
    )
}

val CartProductEntity.cartProduct: CartProduct
    get() {
        return CartProduct(
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
            imageUrl = imageUrl,
            imageUrls = imageUrls,
            isForPayment = isForPayment,
            isSold = isSold,
            isLastInStock = isLastInStock,
            hasActions = hasActions,
            isAlternativesPaletteOpen = isAlternativesPaletteOpen,
            alternatives = alternatives,
            discountPercentage = discountPercentage,
            quantity = quantity,
            sizeCount = sizeCount,
            priceValue = priceValue
        )
    }
