package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.shared.entity.FilterChip
import ru.mercury.vpclient.shared.entity.FilterData
import ru.mercury.vpclient.shared.entity.SortType
import ru.mercury.vpclient.shared.ktx.isFilterValuesDialogChipId
import ru.mercury.vpclient.shared.ktx.isEmpty
import ru.mercury.vpclient.shared.ktx.isRequestAffectingCatalogFilterValueChipId
import ru.mercury.vpclient.shared.mvi.Model
import ru.mercury.vpclient.shared.network.request.CatalogFilterRequest
import ru.mercury.vpclient.shared.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.shared.ktx.brandValues
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_brand.model.FilterBrandSheetState
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_color.model.FilterColorSheetState
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_size.model.FilterSizeSheetState
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_values.model.FilterValuesSheetState

data class FilterModel(
    val filterData: FilterData = FilterData.Empty,
    val selectedSortType: SortType = SortType.OurChoice,
    val selectedFilterValueChips: List<FilterChip> = emptyList(),
    val isRefreshing: Boolean = false,
    val isSortDialogVisible: Boolean = false,
    val isFilterValuesDialogLoading: Boolean = false,
    val filterValuesEntity: FilterValuesEntity = FilterValuesEntity.Empty,
    val filterValuesDialogSelectedValueIds: Set<String> = emptySet(),
    val filterValuesDialogProductsQuantity: FilterValuesQuantityEntity = FilterValuesQuantityEntity.Empty,
    val isFilterValuesDialogProductsQuantityLoading: Boolean = false,
    val filterValuesDialogProductsQuantityJob: Job? = null,
    val filterValuesDialogPickerCollectionJob: Job? = null,
    val filterValuesDialogQuantityCollectionJob: Job? = null,
    val loadProductsQuantityJob: Job? = null
): Model {

    // fixme

    val selectedFilterValueChipIds: Set<String>
        get() = selectedFilterValueChips.map(FilterChip::id).toSet()

    val selectedRequestAffectingFilterValueChipIds: Set<String>
        get() = selectedFilterValueChipIds.filter(String::isRequestAffectingCatalogFilterValueChipId).toSet()

    val filterValuesDialogChips: List<FilterChip>
        get() = filterData.filterRibbonData.topFilterChips
            .plus(filterData.filterRibbonData.bottomFilterChips)
            .filter { chip -> chip.id.isFilterValuesDialogChipId() }

    fun filterValuesDialogChip(chipId: String): FilterChip? {
        return filterValuesDialogChips.firstOrNull { chip -> chip.id == chipId }
    }

    val filterBrandSheetState: FilterBrandSheetState?
        get() {
            val picker = filterValuesEntity
            if (picker.isEmpty || picker.chipId != CatalogFilterRequest.BRAND) {
                return null
            }
            return FilterBrandSheetState(
                brands = picker.brandValues,
                selectedIds = filterValuesDialogSelectedValueIds,
                quantityEntity = filterValuesDialogProductsQuantity,
                isProductsQuantityLoading = isFilterValuesDialogProductsQuantityLoading,
                isLoading = isFilterValuesDialogLoading
            )
        }

    val filterValuesSheetState: FilterValuesSheetState?
        get() {
            val picker = filterValuesEntity
            when {
                picker.isEmpty -> return null
                picker.chipId == CatalogFilterRequest.BRAND -> return null
                picker.chipId == CatalogFilterRequest.COLOR -> return null
                picker.chipId == CatalogFilterRequest.SIZE -> return null
            }
            return FilterValuesSheetState(
                entity = picker.copy(title = picker.title.uppercase()),
                selectedIds = filterValuesDialogSelectedValueIds,
                quantityEntity = filterValuesDialogProductsQuantity,
                isProductsQuantityLoading = isFilterValuesDialogProductsQuantityLoading,
                isLoading = isFilterValuesDialogLoading
            )
        }

    val filterSizeSheetState: FilterSizeSheetState?
        get() {
            val picker = filterValuesEntity
            if (picker.isEmpty || picker.chipId != CatalogFilterRequest.SIZE) {
                return null
            }
            return FilterSizeSheetState(
                entity = picker.copy(title = picker.title),
                selectedIds = filterValuesDialogSelectedValueIds,
                quantityEntity = filterValuesDialogProductsQuantity,
                isProductsQuantityLoading = isFilterValuesDialogProductsQuantityLoading,
                isLoading = isFilterValuesDialogLoading
            )
        }

    val filterColorSheetState: FilterColorSheetState?
        get() {
            val picker = filterValuesEntity
            if (picker.isEmpty || picker.chipId != CatalogFilterRequest.COLOR) {
                return null
            }
            return FilterColorSheetState(
                entity = picker.copy(title = picker.title),
                selectedIds = filterValuesDialogSelectedValueIds,
                quantityEntity = filterValuesDialogProductsQuantity,
                isProductsQuantityLoading = isFilterValuesDialogProductsQuantityLoading,
                isLoading = isFilterValuesDialogLoading
            )
        }

    val isFilterBrandDialogVisible: Boolean
        get() = filterBrandSheetState != null

    val isFilterValuesDialogVisible: Boolean
        get() = filterValuesSheetState != null

    val isFilterSizeDialogVisible: Boolean
        get() = filterSizeSheetState != null

    val isFilterColorDialogVisible: Boolean
        get() = filterColorSheetState != null

    fun selectedFilterValueIds(chipId: String): Set<String> {
        return selectedFilterValueChipIds.filter { valueChipId -> valueChipId.startsWith("${chipId}_") }.toSet()
    }

    fun currentDialogSelectedFilterValueChipIds(): Set<String> {
        val picker = filterValuesEntity
        if (picker.isEmpty) {
            return selectedFilterValueChipIds
        }
        return selectedFilterValueChipIds
            .filterNot { chipId -> chipId.startsWith("${picker.chipId}_") }
            .toSet() + filterValuesDialogSelectedValueIds
    }
}
