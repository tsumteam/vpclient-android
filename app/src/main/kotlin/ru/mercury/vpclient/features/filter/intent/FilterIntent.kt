package ru.mercury.vpclient.features.filter.intent

import ru.mercury.vpclient.features.filter_brand_sheet.intent.FilterBrandIntent
import ru.mercury.vpclient.features.filter_color_sheet.intent.FilterColorIntent
import ru.mercury.vpclient.features.filter_price_sheet.intent.FilterPriceIntent
import ru.mercury.vpclient.features.filter_size_sheet.intent.FilterSizeIntent
import ru.mercury.vpclient.features.filter_tree_sheet.intent.FilterTreeIntent
import ru.mercury.vpclient.features.filter_values_sheet.intent.FilterValuesIntent
import ru.mercury.vpclient.features.sort_picker_sheet.intent.SortPickerIntent
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface FilterIntent: Intent {
    data object CollectRoute: FilterIntent
    data object CollectSearchResultsMetadata: FilterIntent
    data object CollectFilterData: FilterIntent
    data object CollectCartCount: FilterIntent
    data object CollectCartProducts: FilterIntent
    data object CollectFittingCount: FilterIntent
    data object CollectActiveEmployee: FilterIntent
    data object LoadCatalogFilters: FilterIntent
    data object LoadProductsQuantity: FilterIntent
    data object PullToRefresh: FilterIntent
    data object RefreshCompleted: FilterIntent
    data object BackClick: FilterIntent
    data object CartClick: FilterIntent
    data object FittingClick: FilterIntent
    data object MessengerClick: FilterIntent
    data object SearchClick: FilterIntent
    data object ShowSortDialog: FilterIntent
    data object DismissFilterBrandSheet: FilterIntent
    data object DismissFilterPriceSheet: FilterIntent
    data object DismissFilterSizeSheet: FilterIntent
    data object DismissFilterTreeSheet: FilterIntent
    data object DismissFilterColorSheet: FilterIntent
    data object DismissFilterValuesSheet: FilterIntent
    data object ResetFilters: FilterIntent
    data object ConfirmFilterValues: FilterIntent
    data object ToggleBrandFavorited: FilterIntent
    data object InitializeBrandFavoriteStatus: FilterIntent
    data class ProductClick(val id: String): FilterIntent
    data class ProductBasketClick(val product: CatalogFilterProductsEntity): FilterIntent
    data class ShowFilterValuesDialog(val chipId: String): FilterIntent
    data class UpdateFilterValuesSelection(val selectedValueIds: Set<String>): FilterIntent
    data class ToggleFilterValueChip(val chipId: String): FilterIntent
    data class FilterChipClick(val chipId: String): FilterIntent
    data class ToggleFilterDialogValue(val valueId: String): FilterIntent
    data class LoadBrandFavoriteStatus(val brandId: Int, val categoryId: Int): FilterIntent
    data class OnFilterBrandIntent(val intent: FilterBrandIntent): FilterIntent
    data class OnSortPickerIntent(val intent: SortPickerIntent): FilterIntent
    data class OnFilterPriceIntent(val intent: FilterPriceIntent): FilterIntent
    data class OnFilterSizeIntent(val intent: FilterSizeIntent): FilterIntent
    data class OnFilterTreeIntent(val intent: FilterTreeIntent): FilterIntent
    data class OnFilterColorIntent(val intent: FilterColorIntent): FilterIntent
    data class OnFilterValuesIntent(val intent: FilterValuesIntent): FilterIntent
}
