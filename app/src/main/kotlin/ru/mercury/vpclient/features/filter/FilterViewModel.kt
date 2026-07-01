@file:OptIn(ExperimentalCoroutinesApi::class)

package ru.mercury.vpclient.features.filter

import androidx.paging.cachedIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.catalog_root.event.CatalogRootEventManager
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.filter.event.FilterEvent
import ru.mercury.vpclient.features.filter.intent.FilterIntent
import ru.mercury.vpclient.features.filter.model.FilterModel
import ru.mercury.vpclient.features.filter.navigation.FilterRoute
import ru.mercury.vpclient.shared.data.entity.CatalogFilterProductsData
import ru.mercury.vpclient.shared.data.entity.CatalogFilterRequestData2
import ru.mercury.vpclient.shared.data.entity.FilterChip
import ru.mercury.vpclient.shared.data.entity.FilterRequestData
import ru.mercury.vpclient.shared.data.entity.FilterValuesRequestData
import ru.mercury.vpclient.shared.data.entity.SortType
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.persistence.database.RoomException
import ru.mercury.vpclient.shared.data.persistence.database.RoomSQLiteException
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.shared.domain.mapper.includeDefaultCategory
import ru.mercury.vpclient.shared.domain.mapper.isEmpty
import ru.mercury.vpclient.shared.domain.mapper.isRequestAffectingCatalogFilterValueChipId
import ru.mercury.vpclient.shared.domain.mapper.onlyDigits
import ru.mercury.vpclient.shared.domain.mapper.priceFilterChip
import ru.mercury.vpclient.shared.domain.mapper.priceSelectionIds
import ru.mercury.vpclient.shared.domain.mapper.requestFilterValueChipIds
import ru.mercury.vpclient.shared.domain.mapper.toPriceRangeChipData
import ru.mercury.vpclient.shared.domain.mapper.topBarBrandChipId
import ru.mercury.vpclient.shared.domain.mapper.topBarBrandId
import ru.mercury.vpclient.shared.domain.mapper.values
import ru.mercury.vpclient.shared.domain.usecase.BrandFavoriteStatusUseCase
import ru.mercury.vpclient.shared.domain.usecase.CartCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CartProductsFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogFilterProductsPagingDataUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogFilterProductQuantityUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogFilterValuesUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogFiltersUseCase
import ru.mercury.vpclient.shared.domain.usecase.EmployeeActiveFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.FilterDataFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.FilterValuesEntityFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.FilterValuesQuantityEntityFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.FilterValuesQuantityUseCase
import ru.mercury.vpclient.shared.domain.usecase.FittingCountFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.ResetFilterValuesQuantityUseCase
import ru.mercury.vpclient.shared.domain.usecase.ToggleBasketProductUseCase
import ru.mercury.vpclient.shared.domain.usecase.ToggleBrandFavoriteUseCase
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute

@HiltViewModel(assistedFactory = FilterViewModel.Factory::class)
class FilterViewModel @AssistedInject constructor(
    @Assisted private val route: FilterRoute,
    private val filterDataFlowUseCase: FilterDataFlowUseCase,
    private val catalogFiltersUseCase: CatalogFiltersUseCase,
    private val catalogFilterProductQuantityUseCase: CatalogFilterProductQuantityUseCase,
    private val cartCountFlowUseCase: CartCountFlowUseCase,
    private val fittingCountFlowUseCase: FittingCountFlowUseCase,
    private val employeeActiveFlowUseCase: EmployeeActiveFlowUseCase,
    private val toggleBasketProductUseCase: ToggleBasketProductUseCase,
    private val cartProductsFlowUseCase: CartProductsFlowUseCase,
    private val catalogFilterProductsPagingDataUseCase: CatalogFilterProductsPagingDataUseCase,
    private val filterValuesEntityFlowUseCase: FilterValuesEntityFlowUseCase,
    private val filterValuesQuantityEntityFlowUseCase: FilterValuesQuantityEntityFlowUseCase,
    private val catalogFilterValuesUseCase: CatalogFilterValuesUseCase,
    private val filterValuesQuantityUseCase: FilterValuesQuantityUseCase,
    private val resetFilterValuesQuantityUseCase: ResetFilterValuesQuantityUseCase,
    private val brandFavoriteStatusUseCase: BrandFavoriteStatusUseCase,
    private val toggleBrandFavoriteUseCase: ToggleBrandFavoriteUseCase
): ClientViewModel<FilterIntent, FilterModel, FilterEvent>(FilterModel()) {

    val productsPagingFlow = stateFlow
        .map { state ->
            CatalogFilterProductsData(
                categoryId = route.categoryId,
                titleCategoryId = route.titleCategoryId,
                selectedFilterValueChipIds = route.requestFilterValueChipIds(state.selectedRequestAffectingFilterValueChipIds),
                includeDefaultCategory = route.includeDefaultCategory(),
                viewTypeOverride = route.viewTypeOverride,
                sortType = state.selectedSortType
            )
        }
        .distinctUntilChanged()
        .flatMapLatest { entity -> catalogFilterProductsPagingDataUseCase(entity) }
        .cachedIn(this)

    init {
        dispatch(FilterIntent.CollectRoute)
        dispatch(FilterIntent.CollectFilterData)
        dispatch(FilterIntent.CollectCartCount)
        dispatch(FilterIntent.CollectCartProducts)
        dispatch(FilterIntent.CollectFittingCount)
        dispatch(FilterIntent.CollectActiveEmployee)
        dispatch(FilterIntent.LoadCatalogFilters)
        dispatch(FilterIntent.LoadProductsQuantity)
        dispatch(FilterIntent.InitializeBrandFavoriteStatus)
    }

    override fun dispatch(intent: FilterIntent) {
        when (intent) {
            is FilterIntent.CollectRoute -> {
                reduce {
                    it.copy(
                        selectedFilterValueChips = route.initialSelectedFilterValueChips,
                        brandEntity = route.brandEntity,
                        isSingleLineTitle = route.isSingleLineTitle
                    )
                }
            }
            is FilterIntent.CollectFilterData -> {
                launch {
                    filterDataFlowUseCase(
                        FilterRequestData(
                            categoryId = route.categoryId,
                            titleCategoryId = route.titleCategoryId,
                            subtitleCategoryId = route.subtitleCategoryId
                        )
                    ).collectLatest { data ->
                        val topFilterValueChipsById = data.filterRibbonData.topFilterValueChips.associateBy(FilterChip::id)
                        val refreshedSelectedFilterValueChips = stateFlow.value.selectedFilterValueChips.map { chip -> topFilterValueChipsById[chip.id] ?: chip }
                        reduce { it.copy(filterData = data, selectedFilterValueChips = refreshedSelectedFilterValueChips) }
                    }
                }
            }
            is FilterIntent.CollectCartCount -> {
                launch {
                    cartCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count -> reduce { it.copy(cartCount = count) } }
                }
            }
            is FilterIntent.CollectCartProducts -> {
                launch {
                    cartProductsFlowUseCase(Unit).collectLatest { products ->
                        reduce {
                            it.copy(
                                basketProductIds = products.map { product -> product.detailId }
                                    .filter(String::isNotEmpty)
                                    .toSet(),
                                basketProductKeys = products
                                    .filter { product -> product.itemId.isNotEmpty() && product.colorId.isNotEmpty() }
                                    .map { product -> "${product.itemId}:${product.colorId}:${product.sizeId}" }
                                    .toSet()
                            )
                        }
                    }
                }
            }
            is FilterIntent.CollectFittingCount -> {
                launch {
                    fittingCountFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { count -> reduce { it.copy(fittingCount = count) } }
                }
            }
            is FilterIntent.CollectActiveEmployee -> {
                launch {
                    employeeActiveFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { employee ->
                            reduce { it.copy(activeEmployee = employee) }
                        }
                }
            }
            is FilterIntent.LoadCatalogFilters -> {
                launch {
                    catalogFiltersUseCase(
                        CatalogFilterRequestData2(
                            categoryId = route.categoryId,
                            titleCategoryId = route.titleCategoryId,
                            selectedFilterValueChipIds = route.requestFilterValueChipIds(stateFlow.value.selectedRequestAffectingFilterValueChipIds),
                            includeDefaultCategory = route.includeDefaultCategory(),
                            viewTypeOverride = route.viewTypeOverride
                        )
                    ).getOrThrow()
                }
            }
            is FilterIntent.LoadProductsQuantity -> {
                stateFlow.value.loadProductsQuantityJob?.cancel()
                val job = launch {
                    catalogFilterProductQuantityUseCase(
                        CatalogFilterRequestData2(
                            categoryId = route.categoryId,
                            titleCategoryId = route.titleCategoryId,
                            selectedFilterValueChipIds = route.requestFilterValueChipIds(stateFlow.value.selectedRequestAffectingFilterValueChipIds),
                            includeDefaultCategory = route.includeDefaultCategory(),
                            viewTypeOverride = route.viewTypeOverride
                        )
                    ).getOrThrow()
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion { reduce { it.copy(loadProductsQuantityJob = null) } }
                }
                reduce { it.copy(loadProductsQuantityJob = job) }
            }
            is FilterIntent.PullToRefresh -> {
                if (stateFlow.value.isRefreshing) return
                reduce { it.copy(isRefreshing = true) }
                launch { send(FilterEvent.RefreshProducts) }
            }
            is FilterIntent.RefreshCompleted -> reduce { it.copy(isRefreshing = false) }
            is FilterIntent.BackClick -> launch { CatalogRootEventManager.send(BackRoute) }
            is FilterIntent.CartClick -> launch { MainEventManager.send(CartRoute()) }
            is FilterIntent.FittingClick -> {
                launch { MainEventManager.send(CartRoute(CartPage.Fitting)) }
            }
            is FilterIntent.MessengerClick -> return
            is FilterIntent.ProductClick -> {
                launch { CatalogRootEventManager.send(DetailsRoute(intent.id)) }
            }
            is FilterIntent.ProductBasketClick -> {
                launch { toggleBasketProductUseCase(intent.product).getOrThrow() }
            }
            is FilterIntent.ShowSortDialog -> reduce { it.copy(isSortDialogVisible = true) }
            is FilterIntent.HideSortDialog -> reduce { it.copy(isSortDialogVisible = false) }
            is FilterIntent.ShowFilterValuesDialog -> {
                val currentState = stateFlow.value
                val chip = currentState.filterValuesDialogChip(intent.chipId) ?: return
                val selectedValueIds = currentState.selectedFilterValueIds(chip.id)
                val priceRangeChipData = selectedValueIds.firstNotNullOfOrNull { valueId ->
                    valueId.toPriceRangeChipData()
                }
                currentState.filterValuesDialogProductsQuantityJob?.cancel()
                currentState.filterValuesDialogQuantityCollectionJob?.cancel()
                currentState.filterValuesDialogPickerCollectionJob?.cancel()
                reduce {
                    it.copy(
                        isFilterValuesDialogLoading = true,
                        filterValuesEntity = FilterValuesEntity(
                            chipId = chip.id,
                            title = chip.label,
                            items = emptyList()
                        ),
                        filterValuesDialogProductsQuantity = FilterValuesQuantityEntity.Empty,
                        filterValuesDialogSelectedValueIds = selectedValueIds,
                        filterPriceFrom = priceRangeChipData?.from?.toString().orEmpty(),
                        filterPriceTo = priceRangeChipData?.to?.toString().orEmpty(),
                        filterTreePath = emptyList(),
                        filterValuesDialogProductsQuantityJob = null,
                        filterValuesDialogPickerCollectionJob = null,
                        filterValuesDialogQuantityCollectionJob = null
                    )
                }
                val pickerCollectionJob = launch {
                    filterValuesEntityFlowUseCase(chip.id).collectLatest { entity ->
                        reduce {
                            val productsQuantity = when {
                                it.filterValuesDialogProductsQuantity.chipId == entity.chipId &&
                                    it.filterValuesDialogProductsQuantity.quantity != null -> it.filterValuesDialogProductsQuantity
                                else -> FilterValuesQuantityEntity(
                                    chipId = entity.chipId,
                                    quantity = stateFlow.value.filterData.quantityEntity.productsQuantity
                                )
                            }
                            it.copy(
                                isFilterValuesDialogLoading = false,
                                filterValuesEntity = entity.copy(title = chip.label),
                                filterValuesDialogProductsQuantity = productsQuantity
                            )
                        }
                    }
                }
                val quantityCollectionJob = launch {
                    filterValuesQuantityEntityFlowUseCase(chip.id).collectLatest { entity: FilterValuesQuantityEntity ->
                        entity.quantity?.let {
                            reduce { model ->
                                model.copy(
                                    filterValuesDialogProductsQuantity = entity,
                                    isFilterValuesDialogProductsQuantityLoading = false
                                )
                            }
                        }
                    }
                }
                reduce {
                    it.copy(
                        filterValuesDialogPickerCollectionJob = pickerCollectionJob,
                        filterValuesDialogQuantityCollectionJob = quantityCollectionJob
                    )
                }
                launch {
                    catalogFilterValuesUseCase(
                        FilterValuesRequestData(
                            categoryId = route.categoryId,
                            titleCategoryId = route.titleCategoryId,
                            chipId = chip.id,
                            selectedFilterValueChipIds = route.requestFilterValueChipIds(stateFlow.value.selectedRequestAffectingFilterValueChipIds),
                            includeDefaultCategory = route.includeDefaultCategory(),
                            viewTypeOverride = route.viewTypeOverride
                        )
                    ).getOrThrow()
                }
            }
            is FilterIntent.HideFilterValuesDialog -> {
                val currentState = stateFlow.value
                val chipId = currentState.filterValuesEntity.chipId
                currentState.filterValuesDialogProductsQuantityJob?.cancel()
                currentState.filterValuesDialogPickerCollectionJob?.cancel()
                currentState.filterValuesDialogQuantityCollectionJob?.cancel()
                reduce {
                    it.copy(
                        filterValuesEntity = FilterValuesEntity.Empty,
                        filterValuesDialogSelectedValueIds = emptySet(),
                        filterValuesDialogProductsQuantity = FilterValuesQuantityEntity.Empty,
                        filterPriceFrom = "",
                        filterPriceTo = "",
                        filterTreePath = emptyList(),
                        isFilterValuesDialogLoading = false,
                        isFilterValuesDialogProductsQuantityLoading = false,
                        filterValuesDialogProductsQuantityJob = null,
                        filterValuesDialogPickerCollectionJob = null,
                        filterValuesDialogQuantityCollectionJob = null
                    )
                }
                if (chipId.isNotEmpty()) {
                    launch { resetFilterValuesQuantityUseCase(chipId).getOrThrow() }
                }
            }
            is FilterIntent.ResetFilters -> {
                val hasSelectedRequestAffectingFilterValueChips = stateFlow.value.selectedRequestAffectingFilterValueChipIds.isNotEmpty()
                reduce { it.copy(selectedSortType = SortType.OurChoice, selectedFilterValueChips = emptyList()) }
                if (hasSelectedRequestAffectingFilterValueChips) {
                    dispatch(FilterIntent.LoadCatalogFilters)
                    dispatch(FilterIntent.LoadProductsQuantity)
                }
            }
            is FilterIntent.ConfirmSort -> reduce { it.copy(selectedSortType = intent.sortType, isSortDialogVisible = false) }
            is FilterIntent.ConfirmFilterValues -> {
                val currentState = stateFlow.value
                val previousRequestAffectingIds = currentState.selectedRequestAffectingFilterValueChipIds
                val picker = currentState.filterValuesEntity
                val updatedState = when {
                    picker.isEmpty -> {
                        currentState.copy(
                            filterValuesEntity = FilterValuesEntity.Empty,
                            filterValuesDialogProductsQuantity = FilterValuesQuantityEntity.Empty,
                            filterPriceFrom = "",
                            filterPriceTo = "",
                            filterTreePath = emptyList(),
                            isFilterValuesDialogLoading = false,
                            isFilterValuesDialogProductsQuantityLoading = false,
                            filterValuesDialogProductsQuantityJob = null,
                            filterValuesDialogPickerCollectionJob = null,
                            filterValuesDialogQuantityCollectionJob = null
                        )
                    }
                    else -> {
                        val chipId = picker.chipId
                        val selectedValueIds = currentState.filterValuesDialogSelectedValueIds
                        val currentPicker = when {
                            currentState.filterValuesEntity.chipId == chipId && !currentState.filterValuesEntity.isEmpty -> currentState.filterValuesEntity
                            else -> currentState.filterData.filterValuesEntities.firstOrNull { pickerEntity -> pickerEntity.chipId == chipId }
                        }
                        when {
                            currentPicker == null -> {
                                currentState.copy(
                                    filterValuesEntity = FilterValuesEntity.Empty,
                                    filterValuesDialogProductsQuantity = FilterValuesQuantityEntity.Empty,
                                    filterPriceFrom = "",
                                    filterPriceTo = "",
                                    filterTreePath = emptyList(),
                                    isFilterValuesDialogLoading = false,
                                    isFilterValuesDialogProductsQuantityLoading = false,
                                    filterValuesDialogProductsQuantityJob = null,
                                    filterValuesDialogPickerCollectionJob = null,
                                    filterValuesDialogQuantityCollectionJob = null
                                )
                            }
                            else -> {
                                val selectedValueChips = currentPicker.values.filter { chip -> chip.id in selectedValueIds }
                                val preservedSelectedFilterValueChips = currentState.selectedFilterValueChips
                                    .filterNot { chip -> chip.id.startsWith("${chipId}_") }
                                currentState.copy(
                                    selectedFilterValueChips = preservedSelectedFilterValueChips + selectedValueChips,
                                    filterValuesEntity = FilterValuesEntity.Empty,
                                    filterValuesDialogProductsQuantity = FilterValuesQuantityEntity.Empty,
                                    filterPriceFrom = "",
                                    filterPriceTo = "",
                                    filterTreePath = emptyList(),
                                    isFilterValuesDialogLoading = false,
                                    isFilterValuesDialogProductsQuantityLoading = false,
                                    filterValuesDialogProductsQuantityJob = null,
                                    filterValuesDialogPickerCollectionJob = null,
                                    filterValuesDialogQuantityCollectionJob = null
                                )
                            }
                        }
                    }
                }
                currentState.filterValuesDialogProductsQuantityJob?.cancel()
                currentState.filterValuesDialogPickerCollectionJob?.cancel()
                currentState.filterValuesDialogQuantityCollectionJob?.cancel()
                reduce { updatedState }
                if (previousRequestAffectingIds != updatedState.selectedRequestAffectingFilterValueChipIds) {
                    dispatch(FilterIntent.LoadCatalogFilters)
                    dispatch(FilterIntent.LoadProductsQuantity)
                }
            }
            is FilterIntent.ConfirmPrice -> {
                val currentState = stateFlow.value
                val previousRequestAffectingIds = currentState.selectedRequestAffectingFilterValueChipIds
                val chipId = currentState.filterValuesEntity.chipId
                val priceChip = currentState.priceFilterChip()
                currentState.filterValuesDialogProductsQuantityJob?.cancel()
                currentState.filterValuesDialogPickerCollectionJob?.cancel()
                currentState.filterValuesDialogQuantityCollectionJob?.cancel()
                val updatedState = currentState.copy(
                    selectedFilterValueChips = currentState.selectedFilterValueChips
                        .filterNot { chip -> chip.id.startsWith("${chipId}_") }
                        .let { chips -> if (priceChip != null) chips + priceChip else chips },
                    filterValuesEntity = FilterValuesEntity.Empty,
                    filterValuesDialogSelectedValueIds = emptySet(),
                    filterValuesDialogProductsQuantity = FilterValuesQuantityEntity.Empty,
                    filterPriceFrom = "",
                    filterPriceTo = "",
                    filterTreePath = emptyList(),
                    isFilterValuesDialogLoading = false,
                    isFilterValuesDialogProductsQuantityLoading = false,
                    filterValuesDialogProductsQuantityJob = null,
                    filterValuesDialogPickerCollectionJob = null,
                    filterValuesDialogQuantityCollectionJob = null
                )
                reduce { updatedState }
                if (previousRequestAffectingIds != updatedState.selectedRequestAffectingFilterValueChipIds) {
                    dispatch(FilterIntent.LoadCatalogFilters)
                    dispatch(FilterIntent.LoadProductsQuantity)
                }
            }
            is FilterIntent.ResetPrice -> {
                reduce {
                    it.copy(
                        filterPriceFrom = "",
                        filterPriceTo = ""
                    )
                }
                dispatch(FilterIntent.UpdateFilterValuesSelection(emptySet()))
            }
            is FilterIntent.UpdateFilterValuesSelection -> {
                val currentState = stateFlow.value
                val updatedState = currentState.copy(
                    filterValuesDialogSelectedValueIds = intent.selectedValueIds,
                    isFilterValuesDialogProductsQuantityLoading = true,
                    filterValuesDialogProductsQuantityJob = null
                )
                reduce { updatedState }
                currentState.filterValuesDialogProductsQuantityJob?.cancel()
                val picker = updatedState.filterValuesEntity
                if (picker.isEmpty) {
                    return
                }
                val chipId = picker.chipId
                val productsQuantityJob = launch {
                    filterValuesQuantityUseCase(
                        FilterValuesQuantityUseCase.Params(
                            chipId = chipId,
                            data = CatalogFilterRequestData2(
                                categoryId = route.categoryId,
                                titleCategoryId = route.titleCategoryId,
                                selectedFilterValueChipIds = route.requestFilterValueChipIds(stateFlow.value.currentDialogSelectedFilterValueChipIds()),
                                includeDefaultCategory = route.includeDefaultCategory(),
                                viewTypeOverride = route.viewTypeOverride
                            )
                        )
                    ).getOrThrow()
                }.also { job ->
                    job.invokeOnCompletion {
                        reduce { model ->
                            when {
                                model.filterValuesDialogProductsQuantityJob != job -> model
                                model.isFilterValuesDialogVisible ||
                                    model.isFilterColorDialogVisible ||
                                    model.isFilterPriceDialogVisible ||
                                    model.isFilterSizeDialogVisible ||
                                    model.isFilterTreeDialogVisible -> model.copy(
                                    isFilterValuesDialogProductsQuantityLoading = false,
                                    filterValuesDialogProductsQuantityJob = null
                                )
                                else -> model.copy(filterValuesDialogProductsQuantityJob = null)
                            }
                        }
                    }
                }
                reduce { it.copy(filterValuesDialogProductsQuantityJob = productsQuantityJob) }
            }
            is FilterIntent.UpdatePriceFrom -> {
                val priceFrom = intent.value.onlyDigits()
                val updatedPriceTo = stateFlow.value.filterPriceTo.onlyDigits()
                reduce {
                    it.copy(
                        filterPriceFrom = priceFrom,
                        filterPriceTo = updatedPriceTo
                    )
                }
                dispatch(FilterIntent.UpdateFilterValuesSelection(priceSelectionIds(priceFrom, updatedPriceTo)))
            }
            is FilterIntent.UpdatePriceTo -> {
                val updatedPriceFrom = stateFlow.value.filterPriceFrom.onlyDigits()
                val priceTo = intent.value.onlyDigits()
                reduce {
                    it.copy(
                        filterPriceFrom = updatedPriceFrom,
                        filterPriceTo = priceTo
                    )
                }
                dispatch(FilterIntent.UpdateFilterValuesSelection(priceSelectionIds(updatedPriceFrom, priceTo)))
            }
            is FilterIntent.SelectPricePreset -> {
                val priceRangeChipData = intent.valueId.toPriceRangeChipData() ?: return
                val priceFrom = priceRangeChipData.from?.toString().orEmpty()
                val priceTo = priceRangeChipData.to?.toString().orEmpty()
                reduce {
                    it.copy(
                        filterPriceFrom = priceFrom,
                        filterPriceTo = priceTo
                    )
                }
                dispatch(FilterIntent.UpdateFilterValuesSelection(setOf(intent.valueId)))
            }
            is FilterIntent.ToggleFilterValueChip -> {
                val currentState = stateFlow.value
                val previousRequestAffectingIds = currentState.selectedRequestAffectingFilterValueChipIds
                val updatedFilterValueChipIds = when {
                    intent.chipId in currentState.selectedFilterValueChipIds -> currentState.selectedFilterValueChipIds - intent.chipId
                    else -> currentState.selectedFilterValueChipIds + intent.chipId
                }
                val updatedFilterValueChips = currentState.selectedFilterValueChips
                    .filterNot { chip -> chip.id == intent.chipId }
                    .toMutableList()
                    .apply {
                        if (intent.chipId in updatedFilterValueChipIds) {
                            val selectedChip = currentState.filterData.filterRibbonData.topFilterValueChips
                                .firstOrNull { chip -> chip.id == intent.chipId }
                                ?: currentState.selectedFilterValueChips.firstOrNull { chip -> chip.id == intent.chipId }
                            if (selectedChip != null) {
                                add(selectedChip)
                            }
                        }
                    }
                reduce { it.copy(selectedFilterValueChips = updatedFilterValueChips) }
                val updatedRequestAffectingIds = stateFlow.value.selectedRequestAffectingFilterValueChipIds
                if (intent.chipId.isRequestAffectingCatalogFilterValueChipId() && previousRequestAffectingIds != updatedRequestAffectingIds) {
                    dispatch(FilterIntent.LoadCatalogFilters)
                    dispatch(FilterIntent.LoadProductsQuantity)
                }
            }
            is FilterIntent.FilterChipClick -> {
                if (stateFlow.value.filterValuesDialogChip(intent.chipId) != null) {
                    dispatch(FilterIntent.ShowFilterValuesDialog(intent.chipId))
                }
            }
            is FilterIntent.ToggleFilterDialogValue -> {
                val updatedSelectedValueIds = when {
                    intent.valueId in stateFlow.value.filterValuesDialogSelectedValueIds -> stateFlow.value.filterValuesDialogSelectedValueIds - intent.valueId
                    else -> stateFlow.value.filterValuesDialogSelectedValueIds + intent.valueId
                }
                dispatch(FilterIntent.UpdateFilterValuesSelection(updatedSelectedValueIds))
            }
            is FilterIntent.NavigateInFilterTree -> {
                val selectedItem = stateFlow.value.filterValuesEntity.items.firstOrNull { item -> item.id == intent.valueId } ?: return
                if (selectedItem.hasChildren) {
                    reduce { it.copy(filterTreePath = it.filterTreePath + intent.valueId) }
                } else {
                    dispatch(FilterIntent.ToggleFilterDialogValue(intent.valueId))
                }
            }
            is FilterIntent.NavigateBackInFilterTree -> {
                val currentPath = stateFlow.value.filterTreePath
                if (currentPath.isNotEmpty()) {
                    reduce { it.copy(filterTreePath = currentPath.dropLast(1)) }
                }
            }
            is FilterIntent.LoadBrandFavoriteStatus -> {
                launch {
                    val isFavorite = brandFavoriteStatusUseCase(
                        BrandFavoriteStatusUseCase.Params(
                            categoryId = route.categoryId,
                            brandId = intent.brandId
                        )
                    ).getOrThrow()
                    reduce { it.copy(isBrandFavorited = isFavorite) }
                }
            }
            is FilterIntent.InitializeBrandFavoriteStatus -> {
                if (route.brandEntity == null) return
                val brandId = route.topBarBrandId() ?: return
                dispatch(FilterIntent.LoadBrandFavoriteStatus(brandId, route.categoryId))
            }
            is FilterIntent.ToggleBrandFavorited -> {
                val brandId = route.topBarBrandId() ?: return
                val chipId = route.topBarBrandChipId() ?: return
                launch {
                    val currentIsFavorite = stateFlow.value.isBrandFavorited
                    val nextIsFavorite = !currentIsFavorite
                    toggleBrandFavoriteUseCase(
                        ToggleBrandFavoriteUseCase.Params(
                            chipId = chipId,
                            brandId = brandId,
                            categoryId = route.categoryId,
                            isFavorite = nextIsFavorite
                        )
                    ).getOrThrow()
                    reduce { it.copy(isBrandFavorited = nextIsFavorite) }
                }
            }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is RoomException, is RoomSQLiteException -> launch { send(FilterEvent.SnackbarMessage(throwable.message.orEmpty())) }
            is ClientException -> launch { send(FilterEvent.SnackbarMessage(throwable.message)) }
            else -> super.catch(throwable)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: FilterRoute): FilterViewModel
    }
}
