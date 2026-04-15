package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.network.response.CatalogProductSearchCardV2Response
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity

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
        imageUrls = imageUrls.orEmpty(),
        additionalColorPhotoUrls = additionalColors.orEmpty().mapNotNull { it.photoUrl }
    )
}
