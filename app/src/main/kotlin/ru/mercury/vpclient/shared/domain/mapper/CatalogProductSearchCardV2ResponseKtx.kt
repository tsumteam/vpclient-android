package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.network.response.CatalogProductSearchCardV2Response
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductRelatedItemEntity

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

fun CatalogProductSearchCardV2Response.toRelatedItemEntity(): ProductRelatedItemEntity? {
    val id = id ?: return null
    val itemId = itemId ?: return null
    val colorId = colorId ?: return null
    return ProductRelatedItemEntity(
        id = id,
        itemId = itemId,
        colorId = colorId,
        name = name,
        brand = brand,
        urlBrandLogo = urlBrandLogo,
        price = price ?: 0.0,
        priceWithoutDiscount = priceWithoutDiscount,
        imageUrl = imageUrl,
        imageUrls = imageUrls.orEmpty()
    )
}
