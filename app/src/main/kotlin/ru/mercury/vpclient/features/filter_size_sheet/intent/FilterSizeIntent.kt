package ru.mercury.vpclient.features.filter_size_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FilterSizeIntent: Intent {
    data object HideFilterSizeDialog: FilterSizeIntent
    data object ResetFilterSizeValues: FilterSizeIntent
    data object ConfirmFilterSizeValues: FilterSizeIntent
    data class ToggleFilterSizeValue(val valueId: String): FilterSizeIntent
}
