package ru.mercury.vpclient.shared.ktx

import ru.mercury.vpclient.shared.network.response.FiltersResponse
import ru.mercury.vpclient.shared.persistence.database.entity.CatalogCategoryEntity

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
