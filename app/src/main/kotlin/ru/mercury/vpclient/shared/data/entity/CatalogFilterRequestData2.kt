package ru.mercury.vpclient.shared.data.entity

data class CatalogFilterRequestData2( // fixme
    val categoryId: Int,
    val titleCategoryId: Int,
    val selectedFilterValueChipIds: Set<String>
)
