package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_size.intent

sealed interface FilterSizeIntent {
    data object HideFilterSizeDialog: FilterSizeIntent
    data object ResetFilterSizeValues: FilterSizeIntent
    data object ConfirmFilterSizeValues: FilterSizeIntent
    data class ToggleFilterSizeValue(val valueId: String): FilterSizeIntent
}
