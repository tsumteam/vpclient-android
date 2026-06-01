package ru.mercury.vpclient.features.filter_color_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FilterColorIntent: Intent {
    data object HideFilterColorDialog: FilterColorIntent
    data object ResetFilterColorValues: FilterColorIntent
    data object ConfirmFilterColorValues: FilterColorIntent
    data class ToggleFilterColorValue(val valueId: String): FilterColorIntent
}
