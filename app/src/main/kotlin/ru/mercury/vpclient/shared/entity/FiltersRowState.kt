package ru.mercury.vpclient.shared.entity

data class FiltersRowState(
    val filterRibbonData: FilterRibbonData = FilterRibbonData.Empty,
    val sortSelected: Boolean = false,
    val selectedFilterValueChips: List<FilterChip> = emptyList()
) {
    companion object {
        const val SORT_CHIP_KEY = "sort_chip"
        const val RESET_CHIP_KEY = "reset_chip"
    }
}
