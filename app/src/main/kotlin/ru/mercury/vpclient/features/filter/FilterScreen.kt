@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
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
import ru.mercury.vpclient.features.filter.event.FilterEvent
import ru.mercury.vpclient.features.filter.intent.FilterIntent
import ru.mercury.vpclient.features.filter.model.FilterModel
import ru.mercury.vpclient.features.filter.navigation.FilterRoute
import ru.mercury.vpclient.features.filter_brand_sheet.FilterBrandSheet
import ru.mercury.vpclient.features.filter_brand_sheet.intent.FilterBrandIntent
import ru.mercury.vpclient.features.filter_color_sheet.FilterColorSheet
import ru.mercury.vpclient.features.filter_color_sheet.intent.FilterColorIntent
import ru.mercury.vpclient.features.filter_price_sheet.FilterPriceSheet
import ru.mercury.vpclient.features.filter_price_sheet.intent.FilterPriceIntent
import ru.mercury.vpclient.features.filter_size_sheet.FilterSizeSheet
import ru.mercury.vpclient.features.filter_size_sheet.intent.FilterSizeIntent
import ru.mercury.vpclient.features.filter_sort_sheet.FilterSortSheet
import ru.mercury.vpclient.features.filter_sort_sheet.intent.SortIntent
import ru.mercury.vpclient.features.filter_sort_sheet.model.SortModel
import ru.mercury.vpclient.features.filter_tree_sheet.FilterTreeSheet
import ru.mercury.vpclient.features.filter_tree_sheet.intent.FilterTreeIntent
import ru.mercury.vpclient.features.filter_values_sheet.FilterValuesSheet
import ru.mercury.vpclient.features.filter_values_sheet.intent.FilterValuesIntent
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.entity.FilterChip
import ru.mercury.vpclient.shared.data.entity.FilterData
import ru.mercury.vpclient.shared.data.entity.FilterRibbonData
import ru.mercury.vpclient.shared.data.entity.FilterTitleEntity
import ru.mercury.vpclient.shared.data.network.type.CatalogCategoryType
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsQuantityEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValueItemEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.FilterValuesEntity
import ru.mercury.vpclient.shared.domain.mapper.isSortChipSelected
import ru.mercury.vpclient.shared.domain.mapper.productsQuantityWithThousandsSeparator
import ru.mercury.vpclient.shared.domain.mapper.requireProductsQuantity
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.EmptyBox
import ru.mercury.vpclient.shared.ui.components.EmptyBoxState
import ru.mercury.vpclient.shared.ui.components.PagingFailureBox
import ru.mercury.vpclient.shared.ui.components.PagingLoadingBox
import ru.mercury.vpclient.shared.ui.components.SharedPullToRefreshBox
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.brands.BrandBox
import ru.mercury.vpclient.shared.ui.components.cart.CartIconButton
import ru.mercury.vpclient.shared.ui.components.cart.FittingIconButton
import ru.mercury.vpclient.shared.ui.components.cart.MessengerIconButton
import ru.mercury.vpclient.shared.ui.components.filters.FilterProductsLoadingContent
import ru.mercury.vpclient.shared.ui.components.filters.FilterScreenTitle
import ru.mercury.vpclient.shared.ui.components.filters.FiltersRow
import ru.mercury.vpclient.shared.ui.components.filters.FiltersRowState
import ru.mercury.vpclient.shared.ui.components.product.ProductCard
import ru.mercury.vpclient.shared.ui.components.product.ProductCardState
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.icons.Favorited40
import ru.mercury.vpclient.shared.ui.icons.Search24
import ru.mercury.vpclient.shared.ui.icons.Unfavorited40
import ru.mercury.vpclient.shared.ui.icons.VipPlatinumEmpty
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.ktx.isContentVisible
import ru.mercury.vpclient.shared.ui.ktx.isPagingFailure
import ru.mercury.vpclient.shared.ui.ktx.isPagingLoading
import ru.mercury.vpclient.shared.ui.ktx.isRefreshFailure
import ru.mercury.vpclient.shared.ui.ktx.isRefreshLoading
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium18
import ru.mercury.vpclient.shared.ui.theme.regular15
import kotlin.math.max

@Composable
fun FilterScreen(
    route: FilterRoute,
    viewModel: FilterViewModel = hiltViewModel<FilterViewModel, FilterViewModel.Factory>(
        creationCallback = { it.create(route) }
    )
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
                    is FilterBrandIntent.HideFilterBrandDialog -> {
                        viewModel.dispatch(FilterIntent.HideFilterValuesDialog)
                    }
                    is FilterBrandIntent.ResetFilterBrandValues -> {
                        viewModel.dispatch(FilterIntent.UpdateFilterValuesSelection(emptySet()))
                    }
                    is FilterBrandIntent.ConfirmFilterBrandValues -> {
                        viewModel.dispatch(FilterIntent.ConfirmFilterValues)
                    }
                    is FilterBrandIntent.ToggleFilterBrandValue -> {
                        viewModel.dispatch(FilterIntent.ToggleFilterDialogValue(intent.valueId))
                    }
                    is FilterBrandIntent.SelectAllBrands -> {
                        viewModel.dispatch(FilterIntent.UpdateFilterValuesSelection(state.filterValuesDialogSelectedValueIds + intent.valueIds))
                    }
                }
            }
        )
    }
    if (state.isSortDialogVisible) {
        FilterSortSheet(
            state = SortModel(
                selectedSortType = state.selectedSortType
            ),
            dispatch = { intent ->
                when (intent) {
                    is SortIntent.HideSortDialog -> viewModel.dispatch(FilterIntent.HideSortDialog)
                    is SortIntent.ConfirmSort -> viewModel.dispatch(FilterIntent.ConfirmSort(intent.sortType))
                }
            }
        )
    }
    if (state.isFilterPriceDialogVisible) {
        FilterPriceSheet(
            state = requireNotNull(state.filterPriceSheetState),
            dispatch = { intent ->
                when (intent) {
                    is FilterPriceIntent.HideFilterPriceDialog -> viewModel.dispatch(FilterIntent.HideFilterValuesDialog)
                    is FilterPriceIntent.ResetPrice -> viewModel.dispatch(FilterIntent.ResetPrice)
                    is FilterPriceIntent.ConfirmPrice -> viewModel.dispatch(FilterIntent.ConfirmPrice)
                    is FilterPriceIntent.ChangeMinPrice -> viewModel.dispatch(FilterIntent.UpdatePriceFrom(intent.value))
                    is FilterPriceIntent.ChangeMaxPrice -> viewModel.dispatch(FilterIntent.UpdatePriceTo(intent.value))
                    is FilterPriceIntent.SelectPricePreset -> viewModel.dispatch(FilterIntent.SelectPricePreset(intent.valueId))
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
    if (state.isFilterTreeDialogVisible) {
        FilterTreeSheet(
            state = requireNotNull(state.filterTreeSheetState),
            dispatch = { intent ->
                when (intent) {
                    is FilterTreeIntent.HideFilterTreeDialog -> viewModel.dispatch(FilterIntent.HideFilterValuesDialog)
                    is FilterTreeIntent.ResetFilterValues -> viewModel.dispatch(FilterIntent.UpdateFilterValuesSelection(emptySet()))
                    is FilterTreeIntent.ConfirmFilterValues -> viewModel.dispatch(FilterIntent.ConfirmFilterValues)
                    is FilterTreeIntent.NavigateBackInFilterTree -> viewModel.dispatch(FilterIntent.NavigateBackInFilterTree)
                    is FilterTreeIntent.NavigateInFilterTree -> viewModel.dispatch(FilterIntent.NavigateInFilterTree(intent.valueId))
                    is FilterTreeIntent.ToggleFilterValue -> viewModel.dispatch(FilterIntent.ToggleFilterDialogValue(intent.valueId))
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
    val filtersRowLoadingHeightDp = when {
        state.brandEntity != null -> 108.dp + 48.dp
        state.showOnlySortFilter -> 50.dp
        else -> 108.dp
    }
    val filtersRowLoadingHeightPx = with(density) { filtersRowLoadingHeightDp.toPx() }
    var filtersRowHeightPx by remember { mutableFloatStateOf(filtersRowLoadingHeightPx) }
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

    SharedScaffold(
        topBar = {
            val navigationIconsWidth = 2 * 42
            val actionsIconsWidth = (2 + if (state.isFittingButtonVisible) 1 else 0) * 42

            CenterAlignedTopAppBar(
                title = {
                    when {
                        state.titleOverride != null -> {
                            Text(
                                text = state.titleOverride,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.medium18.copy(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                        pagingItems.isRefreshLoading -> {
                            Box(
                                modifier = Modifier
                                    .size(width = 120.dp, height = 24.dp)
                                    .placeholder(
                                        visible = true,
                                        highlight = PlaceholderHighlight.shimmer(),
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            )
                        }
                        state.isFavoriteBrands -> {
                            Text(
                                text = stringResource(ClientStrings.ProfileFavoriteBrands),
                                style = MaterialTheme.typography.medium18.copy(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                        state.brandEntity != null -> {
                            BrandBox(
                                entity = requireNotNull(state.brandEntity),
                                modifier = Modifier.size(width = 120.dp, height = 24.dp)
                            )
                        }
                        state.isSingleLineTitle -> {
                            Text(
                                text = state.filterData.filterTitleEntity.titleCatalogCategoryEntity.name,
                                style = MaterialTheme.typography.medium18.copy(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    textAlign = TextAlign.Center
                                )
                            )
                        }
                        else -> {
                            FilterScreenTitle(
                                entity = state.filterData.filterTitleEntity,
                                onClick = {
                                    filtersRowOffsetPx = 0F
                                    if (pagingItems.isContentVisible) {
                                        scope.launch { gridState.animateScrollToItem(0) }
                                    }
                                },
                                modifier = Modifier.padding(
                                    start = (actionsIconsWidth - navigationIconsWidth).coerceAtLeast(0).dp,
                                    end = (navigationIconsWidth - actionsIconsWidth).coerceAtLeast(0).dp
                                )
                            )
                        }
                    }
                },
                navigationIcon = {
                    Row {
                        IconButton(
                            onClick = { dispatch(FilterIntent.BackClick) },
                            modifier = Modifier.size(42.dp)
                        ) {
                            Icon(
                                imageVector = ChevronStart24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        IconButton(
                            onClick = {},
                            modifier = Modifier.size(42.dp)
                        ) {
                            Icon(
                                imageVector = Search24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                actions = {
                    if (state.isFittingButtonVisible) {
                        FittingIconButton(
                            text = state.fittingText,
                            showBadge = state.isFittingBadgeVisible,
                            onClick = { dispatch(FilterIntent.FittingClick) }
                        )
                    }

                    CartIconButton(
                        text = state.cartText,
                        showBadge = state.isCartBadgeVisible,
                        onClick = { dispatch(FilterIntent.CartClick) }
                    )

                    MessengerIconButton(
                        showBadge = state.isMessengerBadgeVisible,
                        onClick = { dispatch(FilterIntent.MessengerClick) }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        snackbarHost = {
            SharedSnackbarHost(
                hostState = snackbarHostStateError,
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
                        contentPadding = innerPadding + PaddingValues(
                            start = 4.dp,
                            top = filtersRowLoadingHeightDp,
                            end = 4.dp,
                            bottom = 16.dp
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
                    SharedPullToRefreshBox(
                        isRefreshing = state.isRefreshing,
                        onRefresh = { dispatch(FilterIntent.PullToRefresh) },
                        modifier = Modifier.fillMaxSize(),
                        state = pullToRefreshState,
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
                            contentPadding = innerPadding + PaddingValues(
                                start = 4.dp,
                                top = visibleFiltersRowHeightDp,
                                end = 4.dp,
                                bottom = 16.dp
                            )
                        ) {
                            val isEmptyProductsVisible = state.filterData.quantityEntity.requireProductsQuantity == 0 && pagingItems.itemCount == 0
                            item(
                                span = { GridItemSpan(maxLineSpan) }
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = pluralStringResource(
                                            ClientStrings.FilterProductsQuantity,
                                            state.filterData.quantityEntity.requireProductsQuantity,
                                            state.filterData.quantityEntity.productsQuantityWithThousandsSeparator
                                        ),
                                        modifier = Modifier
                                            .padding(top = 8.dp)
                                            .fillMaxWidth(),
                                        style = MaterialTheme.typography.regular15.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 19.sp,
                                            letterSpacing = .2.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    )

                                    if (isEmptyProductsVisible) {
                                        EmptyBox(
                                            state = EmptyBoxState(
                                                imageVector = VipPlatinumEmpty,
                                                text = stringResource(ClientStrings.FilterEmptyProductsMessage),
                                                buttonText = stringResource(ClientStrings.FilterEmptyProductsButton),
                                                onButtonClick = { dispatch(FilterIntent.BackClick) }
                                            ),
                                            modifier = Modifier.padding(top = 85.dp)
                                        )
                                    }
                                }
                            }

                            items(
                                count = pagingItems.itemCount,
                                key = pagingItems.itemKey(),
                                contentType = pagingItems.itemContentType()
                            ) { index ->
                                val entity = pagingItems[index]
                                if (entity != null) {
                                    ProductCard(
                                        state = ProductCardState(
                                            entity = entity,
                                            isInBasket = state.isProductInBasket(entity),
                                            onClick = { dispatch(FilterIntent.ProductClick(entity.id)) },
                                            onBasketIconClick = { dispatch(FilterIntent.ProductBasketClick(entity)) }
                                        )
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
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (state.brandEntity != null) {
                        IconButton(
                            onClick = { dispatch(FilterIntent.ToggleBrandFavorited) },
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.CenterHorizontally)
                                .placeholder(
                                    visible = pagingItems.isRefreshLoading || !state.isBrandFavoritesBarVisible,
                                    highlight = PlaceholderHighlight.shimmer(),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(4.dp)
                                )
                        ) {
                            Icon(
                                imageVector = when {
                                    state.isBrandFavorited == true -> Favorited40
                                    else -> Unfavorited40
                                },
                                contentDescription = null,
                                modifier = Modifier.size(40.dp),
                                tint = Color.Unspecified
                            )
                        }
                    }

                    FiltersRow(
                        state = FiltersRowState(
                            filterRibbonData = when {
                                pagingItems.isRefreshLoading || state.showOnlySortFilter -> FilterRibbonData.Empty
                                else -> state.filterData.filterRibbonData
                            },
                            showOnlySort = state.showOnlySortFilter,
                            sortSelected = !pagingItems.isRefreshLoading && state.selectedSortType.isSortChipSelected,
                            selectedFilterValueChips = when {
                                pagingItems.isRefreshLoading || state.showOnlySortFilter -> emptyList()
                                else -> state.selectedFilterValueChips
                            }
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
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun FilterScreenPreview(
    @PreviewParameter(FilterModelProvider::class) state: FilterPreviewState
) {
    val pagingItems = remember(state) {
        MutableStateFlow(
            PagingData.from(
                data = state.items,
                sourceLoadStates = state.sourceLoadStates
            )
        )
    }.collectAsLazyPagingItems()

    FilterScreenContent(
        state = state.model,
        pagingItems = pagingItems,
        dispatch = {},
        snackbarHostStateError = remember { SnackbarHostState() }
    )
}

private data class FilterPreviewState(
    val model: FilterModel,
    val items: List<CatalogFilterProductsEntity>,
    val sourceLoadStates: LoadStates
)

private class FilterModelProvider: PreviewParameterProvider<FilterPreviewState> {

    private val titleCatalogCategoryEntity = CatalogCategoryEntity(
        id = 10,
        parentId = 2,
        rootId = 2,
        level = CatalogCategoryEntity.LEVEL_TOP,
        name = "Одежда",
        photoUrl = "",
        categoryType = CatalogCategoryType.CATALOG,
        sortSettingId = 0,
        position = 1
    )
    private val subtitleCatalogCategoryEntity = CatalogCategoryEntity(
        id = 1,
        parentId = 10,
        rootId = 2,
        level = CatalogCategoryEntity.LEVEL_BOTTOM,
        name = "ПУХОВЫЕ",
        photoUrl = "",
        categoryType = null,
        sortSettingId = 0,
        position = 1
    )
    private val quantityEntity = CatalogFilterProductsQuantityEntity(
        categoryId = 1,
        titleCategoryId = 1,
        productsQuantity = 5717
    )
    private val emptyQuantityEntity = CatalogFilterProductsQuantityEntity(
        categoryId = 1,
        titleCategoryId = 1,
        productsQuantity = 0
    )
    private val productsEntities = listOf(
        CatalogFilterProductsEntity(
            categoryId = 1,
            titleCategoryId = 11,
            position = 0,
            id = "preview-1",
            itemId = "item-1",
            colorId = "black",
            name = "Кожаная куртка oversize",
            price = 189_900.0,
            priceWithoutDiscount = 234_900.0,
            brand = "SAINT LAURENT",
            urlBrandLogo = "https://example.com/brand-logo.png",
            imageUrl = "",
            imageUrls = listOf("", ""),
            additionalColorPhotoUrls = emptyList()
        ),
        CatalogFilterProductsEntity(
            categoryId = 1,
            titleCategoryId = 11,
            position = 1,
            id = "preview-2",
            itemId = "item-2",
            colorId = "white",
            name = "Хлопковая футболка с логотипом",
            price = 32_700.0,
            priceWithoutDiscount = null,
            brand = "BRUNELLO CUCINELLI",
            urlBrandLogo = null,
            imageUrl = "",
            imageUrls = listOf(""),
            additionalColorPhotoUrls = emptyList()
        ),
        CatalogFilterProductsEntity(
            categoryId = 1,
            titleCategoryId = 11,
            position = 2,
            id = "preview-3",
            itemId = "item-3",
            colorId = "blue",
            name = "Джинсы прямого кроя",
            price = 74_500.0,
            priceWithoutDiscount = 96_400.0,
            brand = "TOM FORD",
            urlBrandLogo = null,
            imageUrl = "",
            imageUrls = emptyList(),
            additionalColorPhotoUrls = emptyList()
        ),
        CatalogFilterProductsEntity(
            categoryId = 1,
            titleCategoryId = 11,
            position = 3,
            id = "preview-4",
            itemId = "item-4",
            colorId = "beige",
            name = "Кашемировый кардиган на пуговицах",
            price = 128_000.0,
            priceWithoutDiscount = 156_800.0,
            brand = "LORO PIANA",
            urlBrandLogo = null,
            imageUrl = "",
            imageUrls = listOf("", "", ""),
            additionalColorPhotoUrls = emptyList()
        )
    )

    private val idleLoadStates = LoadStates(
        refresh = LoadState.NotLoading(endOfPaginationReached = false),
        prepend = LoadState.NotLoading(endOfPaginationReached = false),
        append = LoadState.NotLoading(endOfPaginationReached = false)
    )
    private val loadingLoadStates = LoadStates(
        refresh = LoadState.Loading,
        prepend = LoadState.NotLoading(endOfPaginationReached = false),
        append = LoadState.NotLoading(endOfPaginationReached = false)
    )

    override val values: Sequence<FilterPreviewState> = sequenceOf(
        FilterPreviewState(
            model = FilterModel(
                brandEntity = BrandEntity(
                    brand = "WOOL&COTTON",
                    urlBrandLogo = null
                )
            ),
            items = emptyList(),
            sourceLoadStates = loadingLoadStates
        ),
        FilterPreviewState(
            model = FilterModel(
                filterData = FilterData(
                    filterTitleEntity = FilterTitleEntity(
                        titleCatalogCategoryEntity = titleCatalogCategoryEntity,
                        subtitleCatalogCategoryEntity = subtitleCatalogCategoryEntity
                    ),
                    filterRibbonData = FilterRibbonData(
                        topFilterChips = listOf(
                            FilterChip(id = "brand", label = "Бренд"),
                            FilterChip(id = "size", label = "Размер"),
                            FilterChip(id = "color", label = "Цвет")
                        ),
                        topFilterValueChips = listOf(
                            FilterChip(id = "brand_nike", label = "Nike"),
                            FilterChip(id = "brand_adidas", label = "Adidas")
                        ),
                        bottomFilterChips = listOf(
                            FilterChip(id = "materialAttribute", label = "Материал"),
                            FilterChip(id = "attribute_length", label = "Длина")
                        )
                    ),
                    quantityEntity = quantityEntity,
                    filterValuesEntities = listOf(
                        FilterValuesEntity(
                            chipId = "attribute_length",
                            title = "Длина",
                            items = listOf(
                                FilterValueItemEntity(id = "attribute_length_mini", label = "Мини"),
                                FilterValueItemEntity(id = "attribute_length_midi", label = "Миди"),
                                FilterValueItemEntity(id = "attribute_length_maxi", label = "Макси")
                            )
                        )
                    )
                )
            ),
            items = productsEntities,
            sourceLoadStates = idleLoadStates
        ),
        FilterPreviewState(
            model = FilterModel(
                filterData = FilterData(
                    filterTitleEntity = FilterTitleEntity(
                        titleCatalogCategoryEntity = titleCatalogCategoryEntity,
                        subtitleCatalogCategoryEntity = subtitleCatalogCategoryEntity
                    ),
                    filterRibbonData = FilterRibbonData(
                        topFilterChips = listOf(
                            FilterChip(id = "brand", label = "Бренд"),
                            FilterChip(id = "size", label = "Размер"),
                            FilterChip(id = "color", label = "Цвет")
                        ),
                        topFilterValueChips = emptyList(),
                        bottomFilterChips = emptyList()
                    ),
                    quantityEntity = emptyQuantityEntity
                )
            ),
            items = emptyList(),
            sourceLoadStates = idleLoadStates
        ),
        FilterPreviewState(
            model = FilterModel(
                filterData = FilterData(
                    filterTitleEntity = FilterTitleEntity(
                        titleCatalogCategoryEntity = titleCatalogCategoryEntity,
                        subtitleCatalogCategoryEntity = subtitleCatalogCategoryEntity
                    ),
                    filterRibbonData = FilterRibbonData(
                        topFilterChips = listOf(
                            FilterChip(id = "brand", label = "Бренд"),
                            FilterChip(id = "size", label = "Размер"),
                            FilterChip(id = "color", label = "Цвет")
                        ),
                        topFilterValueChips = emptyList(),
                        bottomFilterChips = emptyList()
                    ),
                    quantityEntity = quantityEntity
                ),
                brandEntity = BrandEntity(
                    brand = "WOOL&COTTON",
                    urlBrandLogo = null
                ),
                isBrandFavorited = true
            ),
            items = productsEntities,
            sourceLoadStates = idleLoadStates
        )
    )
}
