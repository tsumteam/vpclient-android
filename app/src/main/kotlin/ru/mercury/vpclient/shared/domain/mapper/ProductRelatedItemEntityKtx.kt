package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductRelatedItemEntity

fun ProductRelatedItemEntity.toCatalogFilterProductsEntity(position: Int): CatalogFilterProductsEntity {
    return CatalogFilterProductsEntity(
        categoryId = 0,
        titleCategoryId = 0,
        id = id,
        itemId = itemId,
        colorId = colorId,
        name = name.orEmpty(),
        price = price,
        priceWithoutDiscount = priceWithoutDiscount,
        brand = brand.orEmpty(),
        urlBrandLogo = urlBrandLogo,
        imageUrl = imageUrl.orEmpty(),
        imageUrls = imageUrls,
        additionalColorPhotoUrls = emptyList(),
        position = position
    )
}
