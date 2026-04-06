package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.entity.FiltersRowState
import ru.mercury.vpclient.core.ktx.isSortChipSelected
import ru.mercury.vpclient.core.ktx.productsQuantityWithThousandsSeparator
import ru.mercury.vpclient.core.ktx.requireProductsQuantity
import ru.mercury.vpclient.core.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.core.ui.components.catalog.CatalogProductCard
import ru.mercury.vpclient.core.ui.components.filters.FilterProductsLoadingContent
import ru.mercury.vpclient.core.ui.components.filters.FilterScreenTitle
import ru.mercury.vpclient.core.ui.components.filters.FiltersRow
import ru.mercury.vpclient.core.ui.components.IndicatorIconButton
import ru.mercury.vpclient.core.ui.components.PagingFailureBox
import ru.mercury.vpclient.core.ui.components.PagingLoadingBox
import ru.mercury.vpclient.core.ui.components.system.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.core.ui.components.system.ClientSnackbarHost
import ru.mercury.vpclient.core.ui.icons.Basket24
import ru.mercury.vpclient.core.ui.icons.Chat24
import ru.mercury.vpclient.core.ui.icons.ChevronStart24
import ru.mercury.vpclient.core.ui.icons.FittingShirt24
import ru.mercury.vpclient.core.ui.icons.Search24
import ru.mercury.vpclient.core.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.core.ui.ktx.isContentVisible
import ru.mercury.vpclient.core.ui.ktx.isPagingFailure
import ru.mercury.vpclient.core.ui.ktx.isPagingLoading
import ru.mercury.vpclient.core.ui.ktx.isRefreshFailure
import ru.mercury.vpclient.core.ui.ktx.isRefreshLoading
import ru.mercury.vpclient.core.ui.preview.FilterModelProvider
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.regular15
import ru.mercury.vpclient.core.ui.theme.secondary6
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.event.FilterEvent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.intent.FilterIntent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.model.FilterModel
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter.navigation.FilterRoute
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_brand.FilterBrandSheet
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_brand.intent.FilterBrandIntent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_color.FilterColorSheet
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_color.intent.FilterColorIntent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_size.FilterSizeSheet
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_size.intent.FilterSizeIntent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_sort.SortSheet
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_sort.intent.SortIntent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_values.FilterValuesSheet
import ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_values.intent.FilterValuesIntent
import kotlin.math.max

@Composable
fun FilterScreen(
    route: FilterRoute,
    viewModel: FilterViewModel = hiltViewModel<FilterViewModel, FilterViewModel.Factory>(creationCallback = { it.create(route) })
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val pagingItems = viewModel.productsPagingFlow.collectAsLazyPagingItems()
    val scope = rememberCoroutineScope()
    val snackbarHostStateError = remember { SnackbarHostState() }

    FilterScreenContent(
        state = state,
        pagingItems = pagingItems,
        dispatch = viewModel::dispatch,
        snackbarHostStateError = snackbarHostStateError
    )

    if (state.isFilterBrandDialogVisible) {
        FilterBrandSheet(
            state = requireNotNull(state.filterBrandSheetState),
            dispatch = { intent ->
                when (intent) {
                    is FilterBrandIntent.HideFilterBrandDialog -> viewModel.dispatch(FilterIntent.HideFilterValuesDialog)
                    is FilterBrandIntent.ResetFilterBrandValues -> viewModel.dispatch(FilterIntent.UpdateFilterValuesSelection(emptySet()))
                    is FilterBrandIntent.ConfirmFilterBrandValues -> viewModel.dispatch(FilterIntent.ConfirmFilterValues)
                    is FilterBrandIntent.ToggleFilterBrandValue -> viewModel.dispatch(FilterIntent.ToggleFilterDialogValue(intent.valueId))
                    is FilterBrandIntent.SelectAllBrands -> viewModel.dispatch(FilterIntent.UpdateFilterValuesSelection(state.filterValuesDialogSelectedValueIds + intent.valueIds))
                }
            }
        )
    }
    if (state.isSortDialogVisible) {
        SortSheet(
            selectedSortType = state.selectedSortType,
            dispatch = { intent ->
                when (intent) {
                    is SortIntent.HideSortDialog -> viewModel.dispatch(FilterIntent.HideSortDialog)
                    is SortIntent.ConfirmSort -> viewModel.dispatch(FilterIntent.ConfirmSort(intent.sortType))
                }
            }
        )
    }
    if (state.isFilterSizeDialogVisible) {
        FilterSizeSheet(
            state = requireNotNull(state.filterSizeSheetState),
            dispatch = { intent ->
                when (intent) {
                    is FilterSizeIntent.HideFilterSizeDialog -> viewModel.dispatch(FilterIntent.HideFilterValuesDialog)
                    is FilterSizeIntent.ResetFilterSizeValues -> viewModel.dispatch(FilterIntent.UpdateFilterValuesSelection(emptySet()))
                    is FilterSizeIntent.ConfirmFilterSizeValues -> viewModel.dispatch(FilterIntent.ConfirmFilterValues)
                    is FilterSizeIntent.ToggleFilterSizeValue -> viewModel.dispatch(FilterIntent.ToggleFilterDialogValue(intent.valueId))
                }
            }
        )
    }
    if (state.isFilterColorDialogVisible) {
        FilterColorSheet(
            state = requireNotNull(state.filterColorSheetState),
            dispatch = { intent ->
                when (intent) {
                    is FilterColorIntent.HideFilterColorDialog -> viewModel.dispatch(FilterIntent.HideFilterValuesDialog)
                    is FilterColorIntent.ResetFilterColorValues -> viewModel.dispatch(FilterIntent.UpdateFilterValuesSelection(emptySet()))
                    is FilterColorIntent.ConfirmFilterColorValues -> viewModel.dispatch(FilterIntent.ConfirmFilterValues)
                    is FilterColorIntent.ToggleFilterColorValue -> viewModel.dispatch(FilterIntent.ToggleFilterDialogValue(intent.valueId))
                }
            }
        )
    }
    if (state.isFilterValuesDialogVisible) {
        FilterValuesSheet(
            state = requireNotNull(state.filterValuesSheetState),
            dispatch = { intent ->
                when (intent) {
                    is FilterValuesIntent.HideFilterValuesDialog -> viewModel.dispatch(FilterIntent.HideFilterValuesDialog)
                    is FilterValuesIntent.ResetFilterValues -> viewModel.dispatch(FilterIntent.UpdateFilterValuesSelection(emptySet()))
                    is FilterValuesIntent.ConfirmFilterValues -> viewModel.dispatch(FilterIntent.ConfirmFilterValues)
                    is FilterValuesIntent.ToggleFilterValue -> viewModel.dispatch(FilterIntent.ToggleFilterDialogValue(intent.valueId))
                }
            }
        )
    }

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is FilterEvent.RefreshProducts -> pagingItems.refresh()
            is FilterEvent.SnackbarMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
        }
    }

    LaunchedEffect(state.isRefreshing) {
        if (!state.isRefreshing) return@LaunchedEffect
        snapshotFlow { pagingItems.isRefreshLoading }
            .distinctUntilChanged()
            .dropWhile { isLoading -> !isLoading }
            .first { isLoading -> !isLoading }
        viewModel.dispatch(FilterIntent.RefreshCompleted)
    }
}

@Composable
private fun FilterScreenContent(
    state: FilterModel,
    pagingItems: LazyPagingItems<CatalogFilterProductsEntity>,
    dispatch: (FilterIntent) -> Unit,
    snackbarHostStateError: SnackbarHostState
) {
    val density = LocalDensity.current
    val gridState = rememberLazyGridState()
    val pullToRefreshState = rememberPullToRefreshState()
    val scope = rememberCoroutineScope()
    var filtersRowHeightPx by remember { mutableFloatStateOf(0F) }
    var filtersRowOffsetPx by remember { mutableFloatStateOf(0F) }
    val nestedScrollConnection = remember {
        object: NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (filtersRowHeightPx == 0F) return Offset.Zero
                val updatedOffset = (filtersRowOffsetPx - available.y).coerceIn(0F, filtersRowHeightPx)
                filtersRowOffsetPx = updatedOffset
                return Offset.Zero
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ClientCenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                title = {
                    FilterScreenTitle(
                        entity = state.filterData.filterTitleEntity,
                        onClick = {
                            filtersRowOffsetPx = 0F
                            if (pagingItems.isContentVisible) {
                                scope.launch { gridState.animateScrollToItem(0) }
                            }
                        }
                    )
                },
                navigationIcon = {
                    Row {
                        IconButton(
                            onClick = { dispatch(FilterIntent.BackClick) }
                        ) {
                            Icon(
                                imageVector = ChevronStart24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }

                        IconButton(
                            onClick = {}
                        ) {
                            Icon(
                                imageVector = Search24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                },
                actions = {
                    IndicatorIconButton(
                        icon = FittingShirt24,
                        showIndicator = true,
                        onClick = {}
                    )

                    IndicatorIconButton(
                        icon = Basket24,
                        showIndicator = true,
                        onClick = {}
                    )

                    IndicatorIconButton(
                        icon = Chat24,
                        showIndicator = true,
                        onClick = {},
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            )
        },
        snackbarHost = {
            ClientSnackbarHost(
                hostState = snackbarHostStateError,
                modifier = Modifier.padding(horizontal = 16.dp),
                containerColor = MaterialTheme.colorScheme.error
            )
        }
    ) { innerPadding ->
        val visibleFiltersRowHeightDp = with(density) { max(filtersRowHeightPx - filtersRowOffsetPx, 0F).toDp() }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                pagingItems.isRefreshLoading -> {
                    FilterProductsLoadingContent(
                        contentPadding = PaddingValues(
                            start = 4.dp,
                            top = innerPadding.calculateTopPadding() + visibleFiltersRowHeightDp,
                            end = 4.dp,
                            bottom = innerPadding.calculateBottomPadding().plus(16.dp)
                        ),
                        modifier = Modifier.fillMaxSize()
                    )
                }
                pagingItems.isRefreshFailure -> {
                    PagingFailureBox( // fixme
                        onClick = pagingItems::retry,
                        modifier = Modifier
                            .padding(top = innerPadding.calculateTopPadding() + visibleFiltersRowHeightDp)
                            .fillMaxSize()
                    )
                }
                else -> {
                    PullToRefreshBox(
                        isRefreshing = state.isRefreshing,
                        onRefresh = { dispatch(FilterIntent.PullToRefresh) },
                        state = pullToRefreshState,
                        modifier = Modifier.fillMaxSize(),
                        indicator = {
                            PullToRefreshDefaults.Indicator(
                                state = pullToRefreshState,
                                isRefreshing = state.isRefreshing,
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .padding(top = innerPadding.calculateTopPadding() + visibleFiltersRowHeightDp)
                            )
                        }
                    ) {
                        LazyVerticalGrid(
                            state = gridState,
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxSize()
                                .nestedScroll(nestedScrollConnection),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            contentPadding = PaddingValues(
                                start = 4.dp,
                                top = innerPadding.calculateTopPadding() + visibleFiltersRowHeightDp,
                                end = 4.dp,
                                bottom = innerPadding.calculateBottomPadding() + 16.dp
                            )
                        ) {
                            item(
                                span = { GridItemSpan(maxLineSpan) }
                            ) {
                                Text(
                                    text = pluralStringResource(
                                        ClientStrings.FilterProductsQuantity,
                                        state.filterData.quantityEntity.requireProductsQuantity,
                                        state.filterData.quantityEntity.productsQuantityWithThousandsSeparator
                                    ),
                                    modifier = Modifier
                                        .padding(top = 10.dp)
                                        .fillMaxWidth(),
                                    style = MaterialTheme.typography.regular15.copy(
                                        color = MaterialTheme.colorScheme.secondary6,
                                        lineHeight = 19.sp,
                                        letterSpacing = .2.sp,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                            items(
                                count = pagingItems.itemCount,
                                key = pagingItems.itemKey(),
                                contentType = pagingItems.itemContentType()
                            ) { index ->
                                val entity = pagingItems[index]
                                if (entity != null) {
                                    CatalogProductCard(
                                        entity = entity,
                                        onClick = { dispatch(FilterIntent.ProductClick(entity.id)) },
                                        onMessageClick = {},
                                        onBasketClick = {}
                                    )
                                }
                            }

                            when {
                                pagingItems.isPagingLoading -> {
                                    item(
                                        span = { GridItemSpan(maxLineSpan) }
                                    ) {
                                        PagingLoadingBox(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(64.dp)
                                        )
                                    }
                                }
                                pagingItems.isPagingFailure -> {
                                    item(
                                        span = { GridItemSpan(maxLineSpan) }
                                    ) {
                                        PagingFailureBox(
                                            onClick = pagingItems::retry,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(64.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
                    .fillMaxWidth()
                    .graphicsLayer { translationY = -filtersRowOffsetPx }
                    .background(Color.White)
                    .onSizeChanged { size ->
                        filtersRowHeightPx = size.height.toFloat()
                        filtersRowOffsetPx = filtersRowOffsetPx.coerceIn(0F, filtersRowHeightPx)
                    }
            ) {
                FiltersRow(
                    state = FiltersRowState(
                        filterRibbonData = state.filterData.filterRibbonData,
                        sortSelected = state.selectedSortType.isSortChipSelected,
                        selectedFilterValueChips = state.selectedFilterValueChips
                    ),
                    enabled = !pagingItems.isRefreshLoading,
                    onSortClick = { dispatch(FilterIntent.ShowSortDialog) },
                    onFilterChipClick = { chipId -> dispatch(FilterIntent.FilterChipClick(chipId)) },
                    onFilterValueChipClick = { chipId -> dispatch(FilterIntent.ToggleFilterValueChip(chipId)) },
                    onReset = { dispatch(FilterIntent.ResetFilters) }
                )
            }
        }
    }
}

@FontScalePreviews
@Composable
private fun FilterScreenPreview(
    @PreviewParameter(FilterModelProvider::class) state: Pair<FilterModel, List<CatalogFilterProductsEntity>>
) {
    val pagingItems = remember {
        MutableStateFlow(PagingData.from(state.second))
    }.collectAsLazyPagingItems()

    ClientTheme {
        FilterScreenContent(
            state = state.first,
            pagingItems = pagingItems,
            dispatch = {},
            snackbarHostStateError = remember { SnackbarHostState() }
        )
    }
}
