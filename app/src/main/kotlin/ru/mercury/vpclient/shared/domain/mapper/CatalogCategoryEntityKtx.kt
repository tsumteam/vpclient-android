package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.features.filter.navigation.FilterRoute
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.network.type.CatalogViewType
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity

val CatalogCategoryEntity.isEmpty
    get() = this == CatalogCategoryEntity.Empty

val CatalogCategoryEntity.isNotEmpty
    get() = this != CatalogCategoryEntity.Empty

val CatalogCategoryEntity?.orEmpty: CatalogCategoryEntity
    get() = this ?: CatalogCategoryEntity.Empty

val CatalogCategoryEntity.isBasic: Boolean
    get() = level == CatalogCategoryEntity.LEVEL_BASIC

val CatalogCategoryEntity.isTop: Boolean
    get() = level == CatalogCategoryEntity.LEVEL_TOP

fun CatalogCategoryEntity.viewType(categoryId: Int, titleCategoryId: Int): CatalogViewType {
    return when {
        categoryId == titleCategoryId -> CatalogViewType.CATALOG_LEVEL_5
        titleCategoryId == rootId -> CatalogViewType.CATALOG_LEVEL_3
        else -> CatalogViewType.CATALOG_LEVEL_4
    }
}

fun CatalogCategoryEntity.filterRoute(brandEntity: BrandEntity?): FilterRoute {
    return when {
        parentId == null -> {
            FilterRoute(
                categoryId = id,
                titleCategoryId = id,
                subtitleCategoryId = id,
                brandEntity = brandEntity
            )
        }
        level == CatalogCategoryEntity.LEVEL_TOP -> {
            FilterRoute(
                categoryId = id,
                titleCategoryId = rootId,
                subtitleCategoryId = id,
                brandEntity = brandEntity
            )
        }
        level == CatalogCategoryEntity.LEVEL_BOTTOM -> {
            FilterRoute(
                categoryId = id,
                titleCategoryId = parentId,
                subtitleCategoryId = id,
                brandEntity = brandEntity
            )
        }
        else -> {
            FilterRoute(
                categoryId = id,
                titleCategoryId = id,
                subtitleCategoryId = parentId,
                brandEntity = brandEntity
            )
        }
    }
}
