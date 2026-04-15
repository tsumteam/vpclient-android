package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.intent

import ru.mercury.vpclient.shared.data.entity.SortType
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FilterIntent: Intent {
    data object CollectFilterData: FilterIntent
    data object LoadCatalogFilters: FilterIntent
    data object LoadProductsQuantity: FilterIntent
    data object PullToRefresh: FilterIntent
    data object RefreshCompleted: FilterIntent
    data object BackClick: FilterIntent
    data object ShowSortDialog: FilterIntent
    data object HideSortDialog: FilterIntent
    data object HideFilterValuesDialog: FilterIntent
    data object ResetFilters: FilterIntent
    data object ConfirmFilterValues: FilterIntent
    data class ProductClick(val id: String): FilterIntent
    data class ConfirmSort(val sortType: SortType): FilterIntent
    data class ShowFilterValuesDialog(val chipId: String): FilterIntent
    data class UpdateFilterValuesSelection(val selectedValueIds: Set<String>): FilterIntent
    data class ToggleFilterValueChip(val chipId: String): FilterIntent
    data class FilterChipClick(val chipId: String): FilterIntent
    data class ToggleFilterDialogValue(val valueId: String): FilterIntent
}
