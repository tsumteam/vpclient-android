package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.network.response.CatalogProductSearchCardV2Response
import ru.mercury.vpclient.core.persistence.database.entity.CatalogFilterProductsEntity

fun CatalogProductSearchCardV2Response.entity(
    categoryId: Int,
    titleCategoryId: Int,
    position: Int
): CatalogFilterProductsEntity {
    return CatalogFilterProductsEntity(
        categoryId = categoryId,
        titleCategoryId = titleCategoryId,
        position = position,
        id = id.orEmpty(),
        itemId = itemId.orEmpty(),
        colorId = colorId.orEmpty(),
        name = name.orEmpty(),
        price = price.orEmpty,
        priceWithoutDiscount = priceWithoutDiscount,
        brand = brand.orEmpty(),
        urlBrandLogo = urlBrandLogo,
        imageUrl = imageUrl.orEmpty(),
        imageUrls = imageUrls.orEmpty()
    )
}
