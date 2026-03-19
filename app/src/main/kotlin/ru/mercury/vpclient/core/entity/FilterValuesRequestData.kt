package ru.mercury.vpclient.core.entity

data class FilterValuesRequestData(
    val categoryId: Int,
    val titleCategoryId: Int,
    val chipId: String,
    val selectedFilterValueChipIds: Set<String>
)
