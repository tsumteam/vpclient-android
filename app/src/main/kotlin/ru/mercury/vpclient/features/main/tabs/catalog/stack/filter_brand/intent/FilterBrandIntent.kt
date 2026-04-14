package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_brand.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FilterBrandIntent: Intent {
    data object HideFilterBrandDialog: FilterBrandIntent
    data object ResetFilterBrandValues: FilterBrandIntent
    data object ConfirmFilterBrandValues: FilterBrandIntent
    data class ToggleFilterBrandValue(val valueId: String): FilterBrandIntent
    data class SelectAllBrands(val valueIds: Set<String>): FilterBrandIntent
}
