package ru.mercury.vpclient.shared.data.entity

import ru.mercury.vpclient.shared.data.network.type.CatalogViewType

data class CatalogLinkData(
    val viewType: CatalogViewType?,
    val rootCategoryId: Int?,
    val categoryId: Int?,
    val selectedFilterValueChipIds: List<String>,
    val selectedFilterValueChips: List<FilterChip>
)
