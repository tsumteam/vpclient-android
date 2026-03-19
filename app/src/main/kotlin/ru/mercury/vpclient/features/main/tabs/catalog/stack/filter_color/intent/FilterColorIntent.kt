package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_color.intent

sealed interface FilterColorIntent {
    data object HideFilterColorDialog: FilterColorIntent
    data object ResetFilterColorValues: FilterColorIntent
    data object ConfirmFilterColorValues: FilterColorIntent
    data class ToggleFilterColorValue(val valueId: String): FilterColorIntent
}
