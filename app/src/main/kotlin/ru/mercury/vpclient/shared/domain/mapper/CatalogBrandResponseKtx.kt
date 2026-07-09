package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.network.response.CatalogBrandResponse
import ru.mercury.vpclient.shared.data.network.response.CatalogBrandsCategoryResponse
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogBrandEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FavoriteBrandEntity

fun List<CatalogBrandsCategoryResponse>.catalogBrandEntities(
    pairedUserId: String
): List<CatalogBrandEntity> {
    return flatMap { category ->
        category.brands.orEmpty().map { brand ->
            CatalogBrandEntity(
                pairedUserId = pairedUserId,
                categoryId = category.categoryId.orEmpty,
                categoryName = category.categoryName.orEmpty(),
                brandId = brand.id.orEmpty,
                name = brand.name.orEmpty(),
                photoUrl = brand.photoUrl,
                isTopBrand = brand.isTopBrand.orEmpty,
                isFavorite = brand.isFavorite.orEmpty,
                restrictionType = brand.restrictionType
            )
        }
    }
}

fun List<CatalogBrandsCategoryResponse>.entities(
    pairedUserId: String
): List<FavoriteBrandEntity> {
    return flatMap { category ->
        category.brands.orEmpty().mapIndexed { index, brand ->
            brand.entity(
                pairedUserId = pairedUserId,
                category = category,
                position = index
            )
        }
    }
}

private fun CatalogBrandResponse.entity(
    pairedUserId: String,
    category: CatalogBrandsCategoryResponse,
    position: Int
): FavoriteBrandEntity {
    return FavoriteBrandEntity(
        pairedUserId = pairedUserId,
        categoryId = category.categoryId.orEmpty,
        categoryName = category.categoryName.orEmpty(),
        brandId = id.orEmpty,
        name = name.orEmpty(),
        photoUrl = photoUrl,
        isTopBrand = isTopBrand.orEmpty,
        isFavorite = isFavorite.orEmpty,
        restrictionType = restrictionType,
        position = position
    )
}
