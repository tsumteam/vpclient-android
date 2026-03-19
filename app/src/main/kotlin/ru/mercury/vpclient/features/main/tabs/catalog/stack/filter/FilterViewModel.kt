@file:OptIn(ExperimentalCoroutinesApi::class)

package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter

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
import ru.mercury.vpclient.core.RoomException
import ru.mercury.vpclient.core.RoomSQLiteException
import ru.mercury.vpclient.core.entity.CatalogFilterProductsData
import ru.mercury.vpclient.core.entity.CatalogFilterRequestData2
import ru.mercury.vpclient.core.entity.FilterChip
import ru.mercury.vpclient.core.entity.FilterRequestData
import ru.mercury.vpclient.core.entity.FilterValuesRequestData
import ru.mercury.vpclient.core.entity.SortType
import ru.mercury.vpclient.core.exception.ClientException
import ru.mercury.vpclient.core.interactor.Interactor
import ru.mercury.vpclient.core.ktx.isEmpty
import ru.mercury.vpclient.core.ktx.isRequestAffectingCatalogFilterValueChipId
import ru.mercury.vpclient.core.ktx.values
import ru.mercury.vpclient.core.mvi.ClientViewModel
import ru.mercury.vpclient.core.navigation.BackRoute
import ru.mercury.vpclient.core.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.core.persistence.database.entity.FilterValuesQuantityEntity
import ru.mercury.vpclient.features.main.tabs.catalog.event.CatalogStackEventManager
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.event.FilterEvent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.intent.FilterIntent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.model.FilterModel
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.navigation.FilterRoute

// fixme

@HiltViewModel(assistedFactory = FilterViewModel.Factory::class)
class FilterViewModel @AssistedInject constructor(
    @Assisted private val route: FilterRoute,
    private val interactor: Interactor
): ClientViewModel<FilterIntent, FilterModel, FilterEvent>(FilterModel()) {

    val productsPagingFlow = stateFlow
        .map { state ->
            CatalogFilterProductsData(
                categoryId = route.categoryId,
                titleCategoryId = route.titleCategoryId,
                selectedFilterValueChipIds = state.selectedRequestAffectingFilterValueChipIds,
                sortType = state.selectedSortType
            )
        }
        .distinctUntilChanged()
        .flatMapLatest { entity -> interactor.filterProductsPagingData(entity) }
        .cachedIn(this)

    init {
        dispatch(FilterIntent.CollectFilterData)
        dispatch(FilterIntent.LoadCatalogFilters)
        dispatch(FilterIntent.LoadProductsQuantity)
    }

    override fun dispatch(intent: FilterIntent) {
        when (intent) {
            is FilterIntent.CollectFilterData -> {
                launch {
                    interactor.filterDataFlow(
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
            is FilterIntent.LoadCatalogFilters -> {
                launch {
                    interactor.loadCatalogFilters(
                        CatalogFilterRequestData2(
                            categoryId = route.categoryId,
                            titleCategoryId = route.titleCategoryId,
                            selectedFilterValueChipIds = stateFlow.value.selectedRequestAffectingFilterValueChipIds
                        )
                    )
                }
            }
            is FilterIntent.LoadProductsQuantity -> {
                stateFlow.value.loadProductsQuantityJob?.cancel()
                val job = launch {
                    interactor.loadCatalogFiltersProductsQuantity(
                        CatalogFilterRequestData2(
                            categoryId = route.categoryId,
                            titleCategoryId = route.titleCategoryId,
                            selectedFilterValueChipIds = stateFlow.value.selectedRequestAffectingFilterValueChipIds
                        )
                    )
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
            is FilterIntent.BackClick -> launch { CatalogStackEventManager.send(BackRoute) }
            is FilterIntent.ShowSortDialog -> reduce { it.copy(isSortDialogVisible = true) }
            is FilterIntent.HideSortDialog -> reduce { it.copy(isSortDialogVisible = false) }
            is FilterIntent.ShowFilterValuesDialog -> {
                val currentState = stateFlow.value
                val chip = currentState.filterValuesDialogChip(intent.chipId) ?: return
                currentState.filterValuesDialogProductsQuantityJob?.cancel()
                currentState.filterValuesDialogQuantityCollectionJob?.cancel()
                currentState.filterValuesDialogPickerCollectionJob?.cancel()
                reduce {
                    it.copy(
                        isFilterValuesDialogLoading = true,
                        filterValuesEntity = FilterValuesEntity(
                            chipId = chip.id,
                            title = chip.label,
                            valueIds = emptyList(),
                            valueLabels = emptyList()
                        ),
                        filterValuesDialogProductsQuantity = FilterValuesQuantityEntity.Empty,
                        filterValuesDialogSelectedValueIds = it.selectedFilterValueIds(chip.id),
                        filterValuesDialogProductsQuantityJob = null,
                        filterValuesDialogPickerCollectionJob = null,
                        filterValuesDialogQuantityCollectionJob = null
                    )
                }
                val pickerCollectionJob = launch {
                    interactor.filterValuesEntityFlow(chip.id).collectLatest { entity ->
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
                    interactor.filterValuesQuantityEntityFlow(chip.id).collectLatest { entity: FilterValuesQuantityEntity ->
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
                    interactor.loadCatalogFilterValues(
                        FilterValuesRequestData(
                            categoryId = route.categoryId,
                            titleCategoryId = route.titleCategoryId,
                            chipId = chip.id,
                            selectedFilterValueChipIds = stateFlow.value.selectedRequestAffectingFilterValueChipIds
                        )
                    )
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
                        filterValuesDialogProductsQuantity = FilterValuesQuantityEntity.Empty,
                        isFilterValuesDialogLoading = false,
                        isFilterValuesDialogProductsQuantityLoading = false,
                        filterValuesDialogProductsQuantityJob = null,
                        filterValuesDialogPickerCollectionJob = null,
                        filterValuesDialogQuantityCollectionJob = null
                    )
                }
                if (chipId.isNotEmpty()) {
                    launch { interactor.resetFilterValuesQuantity(chipId) }
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
                    interactor.loadCatalogFiltersProductsQuantity(
                        chipId = chipId,
                        data = CatalogFilterRequestData2(
                            categoryId = route.categoryId,
                            titleCategoryId = route.titleCategoryId,
                            selectedFilterValueChipIds = stateFlow.value.currentDialogSelectedFilterValueChipIds()
                        )
                    )
                }.also { job ->
                    job.invokeOnCompletion {
                        reduce { model ->
                            when {
                                model.filterValuesDialogProductsQuantityJob != job -> model
                                model.isFilterValuesDialogVisible || model.isFilterColorDialogVisible || model.isFilterSizeDialogVisible -> model.copy(
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
