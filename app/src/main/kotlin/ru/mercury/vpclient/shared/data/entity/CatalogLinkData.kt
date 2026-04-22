package ru.mercury.vpclient.shared.data.entity

data class CatalogLinkData(
    val viewType: String?,
    val rootCategoryId: Int?,
    val categoryId: Int?,
    val selectedFilterValueChipIds: List<String>,
    val selectedFilterValueChips: List<FilterChip>
)
