package ru.mercury.vpclient.features.filter_tree_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FilterTreeIntent: Intent {
    data object HideFilterTreeDialog: FilterTreeIntent
    data object ResetFilterValues: FilterTreeIntent
    data object ConfirmFilterValues: FilterTreeIntent
    data object NavigateBackInFilterTree: FilterTreeIntent
    data class NavigateInFilterTree(val valueId: String): FilterTreeIntent
    data class ToggleFilterValue(val valueId: String): FilterTreeIntent
}
