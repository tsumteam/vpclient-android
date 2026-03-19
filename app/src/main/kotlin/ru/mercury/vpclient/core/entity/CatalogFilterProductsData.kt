package ru.mercury.vpclient.core.entity

data class CatalogFilterProductsData(
    val categoryId: Int,
    val titleCategoryId: Int,
    val selectedFilterValueChipIds: Set<String>,
    val sortType: SortType
)
