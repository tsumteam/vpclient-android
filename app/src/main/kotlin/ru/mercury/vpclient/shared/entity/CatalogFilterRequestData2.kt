package ru.mercury.vpclient.shared.entity

data class CatalogFilterRequestData2( // fixme
    val categoryId: Int,
    val titleCategoryId: Int,
    val selectedFilterValueChipIds: Set<String>
)
