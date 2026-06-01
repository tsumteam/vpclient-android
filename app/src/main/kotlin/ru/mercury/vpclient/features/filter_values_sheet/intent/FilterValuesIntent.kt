package ru.mercury.vpclient.features.filter_values_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FilterValuesIntent: Intent {
    data object HideFilterValuesDialog: FilterValuesIntent
    data object ResetFilterValues: FilterValuesIntent
    data object ConfirmFilterValues: FilterValuesIntent
    data class ToggleFilterValue(val valueId: String): FilterValuesIntent
}
