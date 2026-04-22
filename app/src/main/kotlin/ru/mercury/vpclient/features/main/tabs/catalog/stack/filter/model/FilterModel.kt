package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.shared.data.entity.FilterChip
import ru.mercury.vpclient.shared.data.entity.FilterData
import ru.mercury.vpclient.shared.data.entity.SortType
import ru.mercury.vpclient.shared.domain.mapper.isFilterValuesDialogChipId
import ru.mercury.vpclient.shared.domain.mapper.isEmpty
import ru.mercury.vpclient.shared.domain.mapper.isRequestAffectingCatalogFilterValueChipId
import ru.mercury.vpclient.shared.mvi.Model
import ru.mercury.vpclient.shared.data.network.request.CatalogFilterRequest
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValueTypeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.shared.domain.mapper.brandValues
import ru.mercury.vpclient.shared.domain.mapper.toPriceRangeChipData
import ru.mercury.vpclient.shared.domain.mapper.values
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_brand.model.FilterBrandSheetState
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_color.model.FilterColorSheetState
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_price.model.FilterPriceSheetState
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_size.model.FilterSizeSheetState
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_tree.model.FilterTreeSheetState
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_tree.model.FilterTreeValue
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_values.model.FilterValuesSheetState

data class FilterModel(
    val filterData: FilterData = FilterData.Empty,
    val selectedSortType: SortType = SortType.OurChoice,
    val selectedFilterValueChips: List<FilterChip> = emptyList(),
    val isBrandFavorited: Boolean = false,
    val isRefreshing: Boolean = false,
    val isSortDialogVisible: Boolean = false,
    val isFilterValuesDialogLoading: Boolean = false,
    val filterValuesEntity: FilterValuesEntity = FilterValuesEntity.Empty,
    val filterValuesDialogSelectedValueIds: Set<String> = emptySet(),
    val filterPriceFrom: String = "",
    val filterPriceTo: String = "",
    val filterTreePath: List<String> = emptyList(),
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
                picker.chipId == CatalogFilterRequest.PRICE -> return null
                picker.chipId == CatalogFilterRequest.SIZE -> return null
                picker.valueType == FilterValueTypeEntity.ID_TREE -> return null
            }
            return FilterValuesSheetState(
                entity = picker.copy(title = picker.title.uppercase()),
                selectedIds = filterValuesDialogSelectedValueIds,
                quantityEntity = filterValuesDialogProductsQuantity,
                isProductsQuantityLoading = isFilterValuesDialogProductsQuantityLoading,
                isLoading = isFilterValuesDialogLoading
            )
        }

    val filterPriceSheetState: FilterPriceSheetState?
        get() {
            val picker = filterValuesEntity
            if (picker.isEmpty || picker.chipId != CatalogFilterRequest.PRICE) {
                return null
            }
            val selectedPresetId = filterValuesDialogSelectedValueIds.firstOrNull { valueId ->
                picker.values.any { chip -> chip.id == valueId }
            }
            return FilterPriceSheetState(
                title = picker.title,
                presets = picker.values,
                selectedPresetId = selectedPresetId,
                priceFrom = filterPriceFrom,
                priceTo = filterPriceTo,
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

    val filterTreeSheetState: FilterTreeSheetState?
        get() {
            val picker = filterValuesEntity
            if (picker.isEmpty || picker.valueType != FilterValueTypeEntity.ID_TREE) {
                return null
            }
            val currentParentId = filterTreePath.lastOrNull()
            val currentParentLabel = picker.items.firstOrNull { item -> item.id == currentParentId }?.label
            val values = picker.items
                .filter { item -> item.parentId == currentParentId }
                .sortedBy { item -> item.order }
                .map { item ->
                    FilterTreeValue(
                        id = item.id,
                        label = item.label,
                        hasChildren = item.hasChildren
                    )
                }
            return FilterTreeSheetState(
                title = picker.title,
                currentParentId = currentParentId,
                currentParentLabel = currentParentLabel,
                values = values,
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

    val isFilterPriceDialogVisible: Boolean
        get() = filterPriceSheetState != null

    val isFilterSizeDialogVisible: Boolean
        get() = filterSizeSheetState != null

    val isFilterTreeDialogVisible: Boolean
        get() = filterTreeSheetState != null

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

    fun filterValuesDialogPriceRange(): Pair<String, String> {
        val priceRangeChipData = filterValuesDialogSelectedValueIds.firstNotNullOfOrNull { valueId ->
            valueId.toPriceRangeChipData()
        }
        return Pair(
            first = priceRangeChipData?.from?.toString().orEmpty(),
            second = priceRangeChipData?.to?.toString().orEmpty()
        )
    }
}
