package ru.mercury.vpclient.core.ktx

import ru.mercury.vpclient.core.persistence.database.entity.CatalogCategoryEntity

val CatalogCategoryEntity.isEmpty
    get() = this == CatalogCategoryEntity.Empty

val CatalogCategoryEntity.isNotEmpty
    get() = this != CatalogCategoryEntity.Empty

val CatalogCategoryEntity.isBasic: Boolean
    get() = level == CatalogCategoryEntity.LEVEL_BASIC
