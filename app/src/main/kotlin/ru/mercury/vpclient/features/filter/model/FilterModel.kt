package ru.mercury.vpclient.features.filter.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.features.filter_brand_sheet.model.FilterBrandModel
import ru.mercury.vpclient.features.filter_color_sheet.model.FilterColorModel
import ru.mercury.vpclient.features.filter_price_sheet.model.FilterPriceModel
import ru.mercury.vpclient.features.filter_size_sheet.model.FilterSizeModel
import ru.mercury.vpclient.features.filter_tree_sheet.model.FilterTreeModel
import ru.mercury.vpclient.features.filter_values_sheet.model.FilterValuesModel
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.entity.FilterChip
import ru.mercury.vpclient.shared.data.entity.FilterData
import ru.mercury.vpclient.shared.data.entity.FilterTreeValue
import ru.mercury.vpclient.shared.data.entity.SortType
import ru.mercury.vpclient.shared.data.network.request.CatalogFilterRequest
import ru.mercury.vpclient.shared.data.network.type.CatalogFilterValueType
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.EmployeeEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.shared.domain.mapper.brandValues
import ru.mercury.vpclient.shared.domain.mapper.hasFittingBadge
import ru.mercury.vpclient.shared.domain.mapper.hasMessengerBadge
import ru.mercury.vpclient.shared.domain.mapper.isEmpty
import ru.mercury.vpclient.shared.domain.mapper.isFilterValuesDialogChipId
import ru.mercury.vpclient.shared.domain.mapper.isRequestAffectingCatalogFilterValueChipId
import ru.mercury.vpclient.shared.domain.mapper.toPriceRangeChipData
import ru.mercury.vpclient.shared.domain.mapper.values
import ru.mercury.vpclient.shared.mvi.Model

data class FilterModel(
    val filterData: FilterData = FilterData.Empty,
    val selectedSortType: SortType = SortType.OurChoice,
    val selectedFilterValueChips: List<FilterChip> = emptyList(),
    val brandEntity: BrandEntity? = null,
    val titleOverride: String? = null,
    val isSingleLineTitle: Boolean = false,
    val isFavoriteBrands: Boolean = false,
    val showOnlySortFilter: Boolean = false,
    val isBrandFavorited: Boolean? = null,
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
    val loadProductsQuantityJob: Job? = null,
    val basketProductIds: Set<String> = emptySet(),
    val basketProductKeys: Set<String> = emptySet(),
    val cartCount: Int = 0,
    val cartBadge: Int = 0,
    val fittingCount: Int = 0,
    val activeEmployee: EmployeeEntity = EmployeeEntity.Empty
): Model {

    val isBrandFavoritesBarVisible: Boolean
        get() = brandEntity != null && isBrandFavorited != null

    val cartText: String
        get() = when {
            cartCount > 0 -> cartCount.toString()
            else -> ""
        }

    val isCartBadgeVisible: Boolean
        get() = cartBadge > 0

    val fittingText: String
        get() = if (fittingCount > 0) fittingCount.toString() else ""

    val isFittingButtonVisible: Boolean
        get() = fittingCount > 0

    val isFittingBadgeVisible: Boolean
        get() = activeEmployee.hasFittingBadge

    val isMessengerBadgeVisible: Boolean
        get() = activeEmployee.hasMessengerBadge

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

    val filterBrandSheetState: FilterBrandModel?
        get() {
            val picker = filterValuesEntity
            if (picker.isEmpty || picker.chipId != CatalogFilterRequest.BRAND) {
                return null
            }
            return FilterBrandModel(
                brands = picker.brandValues,
                selectedIds = filterValuesDialogSelectedValueIds,
                quantityEntity = filterValuesDialogProductsQuantity,
                isProductsQuantityLoading = isFilterValuesDialogProductsQuantityLoading,
                isLoading = isFilterValuesDialogLoading
            )
        }

    val filterValuesSheetState: FilterValuesModel?
        get() {
            val picker = filterValuesEntity
            when {
                picker.isEmpty -> return null
                picker.chipId == CatalogFilterRequest.BRAND -> return null
                picker.chipId == CatalogFilterRequest.COLOR -> return null
                picker.chipId == CatalogFilterRequest.PRICE -> return null
                picker.chipId == CatalogFilterRequest.SIZE -> return null
                picker.valueType == CatalogFilterValueType.ID_TREE -> return null
            }
            return FilterValuesModel(
                entity = picker.copy(title = picker.title.uppercase()),
                selectedIds = filterValuesDialogSelectedValueIds,
                quantityEntity = filterValuesDialogProductsQuantity,
                isProductsQuantityLoading = isFilterValuesDialogProductsQuantityLoading,
                isLoading = isFilterValuesDialogLoading
            )
        }

    val filterPriceSheetState: FilterPriceModel?
        get() {
            val picker = filterValuesEntity
            if (picker.isEmpty || picker.chipId != CatalogFilterRequest.PRICE) {
                return null
            }
            val selectedPresetId = filterValuesDialogSelectedValueIds.firstOrNull { valueId ->
                picker.values.any { chip -> chip.id == valueId }
            }
            return FilterPriceModel(
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

    val filterSizeSheetState: FilterSizeModel?
        get() {
            val picker = filterValuesEntity
            if (picker.isEmpty || picker.chipId != CatalogFilterRequest.SIZE) {
                return null
            }
            return FilterSizeModel(
                entity = picker.copy(title = picker.title),
                selectedIds = filterValuesDialogSelectedValueIds,
                quantityEntity = filterValuesDialogProductsQuantity,
                isProductsQuantityLoading = isFilterValuesDialogProductsQuantityLoading,
                isLoading = isFilterValuesDialogLoading
            )
        }

    val filterTreeSheetState: FilterTreeModel?
        get() {
            val picker = filterValuesEntity
            if (picker.isEmpty || picker.valueType != CatalogFilterValueType.ID_TREE) {
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
            return FilterTreeModel(
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

    val filterColorSheetState: FilterColorModel?
        get() {
            val picker = filterValuesEntity
            if (picker.isEmpty || picker.chipId != CatalogFilterRequest.COLOR) {
                return null
            }
            return FilterColorModel(
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

    fun isProductInBasket(entity: CatalogFilterProductsEntity): Boolean {
        val productKeyPrefix = "${entity.itemId}:${entity.colorId}:"
        return basketProductKeys.any { key -> key.startsWith(productKeyPrefix) }
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
