package ru.mercury.vpclient.core.entity

data class FilterRibbonData(
    val topFilterChips: List<FilterChip>,
    val topFilterValueChips: List<FilterChip>,
    val bottomFilterChips: List<FilterChip>
) {
    companion object {
        val Empty = FilterRibbonData(
            topFilterChips = emptyList(),
            topFilterValueChips = emptyList(),
            bottomFilterChips = emptyList()
        )
    }
}
