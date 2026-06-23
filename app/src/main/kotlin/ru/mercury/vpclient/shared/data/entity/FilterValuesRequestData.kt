package ru.mercury.vpclient.shared.data.entity

import ru.mercury.vpclient.shared.data.network.type.CatalogViewType

data class FilterValuesRequestData(
    val categoryId: Int,
    val titleCategoryId: Int,
    val chipId: String,
    val selectedFilterValueChipIds: Set<String>,
    val includeDefaultCategory: Boolean = true,
    val viewTypeOverride: CatalogViewType? = null
)
