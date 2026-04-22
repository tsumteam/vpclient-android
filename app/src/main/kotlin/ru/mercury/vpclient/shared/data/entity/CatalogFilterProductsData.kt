package ru.mercury.vpclient.shared.data.entity

data class CatalogFilterProductsData(
    val categoryId: Int,
    val titleCategoryId: Int,
    val selectedFilterValueChipIds: Set<String>,
    val includeDefaultCategory: Boolean = true,
    val viewTypeOverride: String? = null,
    val sortType: SortType
)
