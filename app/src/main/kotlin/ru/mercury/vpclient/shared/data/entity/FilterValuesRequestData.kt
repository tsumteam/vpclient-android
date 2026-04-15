package ru.mercury.vpclient.shared.data.entity

data class FilterValuesRequestData(
    val categoryId: Int,
    val titleCategoryId: Int,
    val chipId: String,
    val selectedFilterValueChipIds: Set<String>
)
