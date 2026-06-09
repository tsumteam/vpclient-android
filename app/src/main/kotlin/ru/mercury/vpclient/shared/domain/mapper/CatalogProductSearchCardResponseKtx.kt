package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.network.response.CatalogProductSearchCardResponse
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity

fun CatalogProductSearchCardResponse.toCatalogFilterProductsEntity(
    position: Int,
    categoryId: Int = 0,
    titleCategoryId: Int = 0
): CatalogFilterProductsEntity {
    return CatalogFilterProductsEntity(
        categoryId = categoryId,
        titleCategoryId = titleCategoryId,
        id = id.orEmpty(),
        itemId = itemId.orEmpty(),
        colorId = colorId.orEmpty(),
        name = name.orEmpty(),
        price = price.orEmpty,
        priceWithoutDiscount = priceWithoutDiscount,
        brand = brand.orEmpty(),
        urlBrandLogo = urlBrandLogo,
        imageUrl = imageUrl.orEmpty(),
        imageUrls = imageUrls.orEmpty(),
        additionalColorPhotoUrls = emptyList(),
        position = position
    )
}
