package ru.mercury.vpclient.shared.entity

data class CatalogFilterProductsData(
    val categoryId: Int,
    val titleCategoryId: Int,
    val selectedFilterValueChipIds: Set<String>,
    val sortType: SortType
)
