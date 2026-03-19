package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_values.intent

sealed interface FilterValuesIntent {
    data object HideFilterValuesDialog: FilterValuesIntent
    data object ResetFilterValues: FilterValuesIntent
    data object ConfirmFilterValues: FilterValuesIntent
    data class ToggleFilterValue(val valueId: String): FilterValuesIntent
}
