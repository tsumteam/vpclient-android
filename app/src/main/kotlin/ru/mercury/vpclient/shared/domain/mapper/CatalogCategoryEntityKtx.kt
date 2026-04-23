package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.navigation.FilterRoute
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.network.response.FiltersResponse
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity

const val BRAND_VIEW_TYPE = "brand"

val CatalogCategoryEntity.isEmpty
    get() = this == CatalogCategoryEntity.Empty

val CatalogCategoryEntity.isNotEmpty
    get() = this != CatalogCategoryEntity.Empty

val CatalogCategoryEntity.isBasic: Boolean
    get() = level == CatalogCategoryEntity.LEVEL_BASIC

val CatalogCategoryEntity.isTop: Boolean
    get() = level == CatalogCategoryEntity.LEVEL_TOP

fun CatalogCategoryEntity.viewType(categoryId: Int, titleCategoryId: Int): String {
    return when {
        categoryId == titleCategoryId -> FiltersResponse.VIEW_TYPE_CATALOG_LEVEL_5
        titleCategoryId == rootId -> FiltersResponse.VIEW_TYPE_CATALOG_LEVEL_3
        else -> FiltersResponse.VIEW_TYPE_CATALOG_LEVEL_4
    }
}

fun CatalogCategoryEntity.toFilterRoute(brandEntity: BrandEntity?): FilterRoute? {
    return when {
        parentId == null -> FilterRoute(
            categoryId = id,
            titleCategoryId = id,
            subtitleCategoryId = id,
            brandEntity = brandEntity
        )
        level == CatalogCategoryEntity.LEVEL_TOP -> FilterRoute(
            categoryId = id,
            titleCategoryId = rootId,
            subtitleCategoryId = id,
            brandEntity = brandEntity
        )
        level == CatalogCategoryEntity.LEVEL_BOTTOM -> FilterRoute(
            categoryId = id,
            titleCategoryId = parentId,
            subtitleCategoryId = id,
            brandEntity = brandEntity
        )
        else -> FilterRoute(
            categoryId = id,
            titleCategoryId = id,
            subtitleCategoryId = parentId,
            brandEntity = brandEntity
        )
    }
}