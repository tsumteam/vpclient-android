@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.brands

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.brands.event.BrandsEvent
import ru.mercury.vpclient.features.brands.intent.BrandsIntent
import ru.mercury.vpclient.features.brands.model.BrandsModel
import ru.mercury.vpclient.shared.data.entity.BrandsPage
import ru.mercury.vpclient.shared.data.entity.BrandsSection
import ru.mercury.vpclient.shared.data.entity.TabType
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogBrandEntity
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.SharedAnimatedVisibility
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.brands.BrandAlphabetScrubber
import ru.mercury.vpclient.shared.ui.components.brands.BrandAlphabetScrubberState
import ru.mercury.vpclient.shared.ui.components.brands.BrandFavoriteIconButton
import ru.mercury.vpclient.shared.ui.components.brands.BrandFavoriteIconButtonState
import ru.mercury.vpclient.shared.ui.components.brands.BrandFavoriteRow
import ru.mercury.vpclient.shared.ui.components.brands.BrandFavoriteRowState
import ru.mercury.vpclient.shared.ui.components.brands.BrandGrid
import ru.mercury.vpclient.shared.ui.components.brands.BrandGridState
import ru.mercury.vpclient.shared.ui.components.brands.BrandSectionHeader
import ru.mercury.vpclient.shared.ui.components.brands.BrandSectionHeaderState
import ru.mercury.vpclient.shared.ui.components.cart.CartIconButton
import ru.mercury.vpclient.shared.ui.components.cart.FittingIconButton
import ru.mercury.vpclient.shared.ui.components.cart.MessengerIconButton
import ru.mercury.vpclient.shared.ui.components.catalog.CatalogTabData
import ru.mercury.vpclient.shared.ui.components.catalog.TabRow
import ru.mercury.vpclient.shared.ui.icons.Search24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium18

@Composable
fun BrandsScreen(
    viewModel: BrandsViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostStateError = remember { SnackbarHostState() }

    BrandsScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        snackbarHostStateError = snackbarHostStateError
    )

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is BrandsEvent.SnackbarErrorMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
        }
    }
}

@Composable
private fun BrandsScreenContent(
    state: BrandsModel,
    dispatch: (BrandsIntent) -> Unit,
    snackbarHostStateError: SnackbarHostState
) {
    val tabs = TabType.entries.map { tab ->
        CatalogTabData(
            title = stringResource(
                when (tab) {
                    TabType.WOMAN -> ClientStrings.ProfileBrandsWomanTabCaps
                    TabType.MAN -> ClientStrings.ProfileBrandsManTabCaps
                    TabType.CHILD -> ClientStrings.ProfileBrandsChildTabCaps
                }
            ),
            rootId = tab.value,
            selected = state.selectedTab == tab
        )
    }
    val selectedTabIndex = TabType.entries.indexOf(state.selectedTab)
    val pagerState = rememberPagerState(
        initialPage = selectedTabIndex.coerceAtLeast(0),
        pageCount = { TabType.entries.size }
    )
    val scope = rememberCoroutineScope()

    LaunchedEffect(selectedTabIndex) {
        when {
            selectedTabIndex in TabType.entries.indices && pagerState.currentPage != selectedTabIndex -> {
                pagerState.scrollToPage(selectedTabIndex)
            }
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                TabType.entries
                    .getOrNull(page)
                    ?.let { tab -> dispatch(BrandsIntent.SelectTab(tab)) }
            }
    }

    SharedScaffold(
        topBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(ClientStrings.MainTabBrands),
                            style = MaterialTheme.typography.medium18
                        )
                    },
                    navigationIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { dispatch(BrandsIntent.SearchClick) },
                                modifier = Modifier.size(42.dp)
                            ) {
                                Icon(
                                    imageVector = Search24,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            BrandFavoriteIconButton(
                                state = BrandFavoriteIconButtonState(
                                    text = state.favoriteBrandsText,
                                    visible = state.isFavoriteBrandsButtonVisible,
                                    onClick = { dispatch(BrandsIntent.FavoriteBrandsClick) }
                                )
                            )
                        }
                    },
                    actions = {
                        if (state.isFittingButtonVisible) {
                            FittingIconButton(
                                text = state.fittingText,
                                showBadge = state.isFittingBadgeVisible,
                                onClick = { dispatch(BrandsIntent.FittingClick) }
                            )
                        }

                        CartIconButton(
                            text = state.cartText,
                            showBadge = state.isCartBadgeVisible,
                            onClick = { dispatch(BrandsIntent.CartClick) }
                        )

                        MessengerIconButton(
                            showBadge = state.isMessengerBadgeVisible,
                            onClick = { dispatch(BrandsIntent.MessengerClick) }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                        actionIconContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )

                TabRow(
                    tabs = tabs,
                    selectedTabIndex = pagerState.currentPage,
                    onTabClick = { index -> scope.launch { pagerState.animateScrollToPage(index) } }
                )
            }
        },
        snackbarHost = {
            SharedSnackbarHost(
                hostState = snackbarHostStateError,
                containerColor = MaterialTheme.colorScheme.error
            )
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            val brandsPage = state.pages[page]

            when {
                brandsPage.catalogBrandEntities.isEmpty() -> {
                    SharedLazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        userScrollEnabled = false
                    ) {
                        item {
                            Spacer(
                                modifier = Modifier
                                    .width(112.dp)
                                    .height(20.dp)
                                    .placeholder(
                                        visible = true,
                                        highlight = PlaceholderHighlight.shimmer(),
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            )
                        }
                        item {
                            Column(
                                modifier = Modifier.padding(top = 16.dp, bottom = 20.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                repeat(2) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        repeat(3) {
                                            Spacer(
                                                modifier = Modifier
                                                    .weight(1F)
                                                    .height(46.dp)
                                                    .placeholder(
                                                        visible = true,
                                                        highlight = PlaceholderHighlight.shimmer(),
                                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                                        shape = RoundedCornerShape(4.dp)
                                                    )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            Spacer(
                                modifier = Modifier
                                    .width(112.dp)
                                    .height(20.dp)
                                    .placeholder(
                                        visible = true,
                                        highlight = PlaceholderHighlight.shimmer(),
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            )
                        }
                        item { Spacer(modifier = Modifier.height(8.dp)) }
                        items(4) {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .height(48.dp)
                                    .placeholder(
                                        visible = true,
                                        highlight = PlaceholderHighlight.shimmer(),
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(4.dp)
                                    )
                            )
                        }
                    }
                }
                else -> {
                    val lazyListState = rememberLazyListState()
                    val allBrandsHeaderIndex = remember(
                        brandsPage.favoriteBrandEntities,
                        brandsPage.topBrandEntities
                    ) {
                        var index = 0
                        when {
                            brandsPage.favoriteBrandEntities.isNotEmpty() -> index += 1
                        }
                        when {
                            brandsPage.topBrandEntities.isNotEmpty() -> index += 1
                        }
                        index
                    }
                    val letterIndices = remember(brandsPage.sections, allBrandsHeaderIndex) {
                        buildMap {
                            var index = allBrandsHeaderIndex + 1
                            brandsPage.sections.forEach { section ->
                                put(section.letter, index)
                                index += section.catalogBrandEntities.size + 1
                            }
                        }
                    }
                    val isAlphabetVisible by remember(lazyListState, allBrandsHeaderIndex) {
                        derivedStateOf {
                            lazyListState.firstVisibleItemIndex >= allBrandsHeaderIndex
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        SharedLazyColumn(
                            state = lazyListState,
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(vertical = 16.dp)
                        ) {
                            if (brandsPage.favoriteBrandEntities.isNotEmpty()) {
                                item(
                                    key = "favorite_brands"
                                ) {
                                    BrandGrid(
                                        state = BrandGridState(
                                            headerState = BrandSectionHeaderState(
                                                title = stringResource(ClientStrings.BrandsFavoritesHeader),
                                                showSelectAll = false,
                                                onSelectAll = {}
                                            ),
                                            catalogBrandEntities = brandsPage.favoriteBrandEntities,
                                            onBrandClick = { entity -> dispatch(BrandsIntent.BrandClick(entity)) }
                                        ),
                                        modifier = Modifier
                                            .animateItem()
                                            .padding(bottom = 16.dp)
                                    )
                                }
                            }
                            if (brandsPage.topBrandEntities.isNotEmpty()) {
                                item(
                                    key = "top_brands"
                                ) {
                                    BrandGrid(
                                        state = BrandGridState(
                                            headerState = BrandSectionHeaderState(
                                                title = stringResource(ClientStrings.FilterBrandTopHeader),
                                                showSelectAll = false,
                                                onSelectAll = {}
                                            ),
                                            catalogBrandEntities = brandsPage.topBrandEntities,
                                            onBrandClick = { entity -> dispatch(BrandsIntent.BrandClick(entity)) }
                                        ),
                                        modifier = Modifier
                                            .animateItem()
                                            .padding(bottom = 16.dp)
                                    )
                                }
                            }
                            item {
                                BrandSectionHeader(
                                    state = BrandSectionHeaderState(
                                        title = stringResource(ClientStrings.FilterBrandAllHeader),
                                        showSelectAll = false,
                                        onSelectAll = {}
                                    )
                                )
                            }
                            brandsPage.sections.forEach { section ->
                                stickyHeader(
                                    key = "header_${section.letter}"
                                ) {
                                    Text(
                                        text = section.letter,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(44.dp)
                                            .background(MaterialTheme.colorScheme.background)
                                            .padding(start = 16.dp)
                                            .wrapContentHeight(Alignment.CenterVertically),
                                        style = MaterialTheme.typography.livretMedium18.copy(
                                            color = MaterialTheme.colorScheme.error,
                                            lineHeight = 26.sp,
                                            letterSpacing = .2.sp
                                        )
                                    )
                                }
                                itemsIndexed(
                                    items = section.catalogBrandEntities,
                                    key = { _, entity -> entity.brandId }
                                ) { index, entity ->
                                    BrandFavoriteRow(
                                        state = BrandFavoriteRowState(
                                            entity = entity,
                                            enabled = entity.brandId !in state.saveFavoriteBrandJobs,
                                            onClick = { dispatch(BrandsIntent.BrandClick(entity)) },
                                            onFavoriteClick = { dispatch(BrandsIntent.FavoriteClick(entity)) }
                                        )
                                    )

                                    if (index != section.catalogBrandEntities.lastIndex) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(horizontal = 16.dp),
                                            color = MaterialTheme.colorScheme.outlineVariant
                                        )
                                    }
                                }
                            }
                        }

                        SharedAnimatedVisibility(
                            visible = isAlphabetVisible,
                            enter = fadeIn() + slideInHorizontally { width -> width },
                            exit = fadeOut() + slideOutHorizontally { width -> width },
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            BrandAlphabetScrubber(
                                state = BrandAlphabetScrubberState(
                                    letters = brandsPage.sections.map { section -> section.letter },
                                    onLetterSelect = { letter ->
                                        letterIndices[letter]?.let { index ->
                                            scope.launch {
                                                lazyListState.scrollToItem(
                                                    index = index,
                                                    scrollOffset = 1
                                                )
                                            }
                                        }
                                    }
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun BrandsScreenContentPreview(
    @PreviewParameter(BrandsModelProvider::class) state: BrandsModel
) {
    BrandsScreenContent(
        state = state,
        dispatch = {},
        snackbarHostStateError = remember { SnackbarHostState() }
    )
}

private class BrandsModelProvider: PreviewParameterProvider<BrandsModel> {
    private val entities = listOf(
        CatalogBrandEntity("", 2, "Женское", 1, "Alexander McQueen", null, false, false, null),
        CatalogBrandEntity("", 2, "Женское", 2, "Balenciaga", null, true, true, null),
        CatalogBrandEntity("", 2, "Женское", 3, "LOEWE", null, true, false, null)
    )

    private val contentPage = BrandsPage(
        tab = TabType.WOMAN,
        catalogBrandEntities = entities,
        favoriteBrandEntities = entities.filter { entity -> entity.isFavorite },
        topBrandEntities = entities.filter { entity -> entity.isTopBrand && !entity.isFavorite },
        sections = listOf(
            BrandsSection("A", listOf(entities[0])),
            BrandsSection("B", listOf(entities[1])),
            BrandsSection("L", listOf(entities[2]))
        )
    )

    override val values: Sequence<BrandsModel> = sequenceOf(
        BrandsModel(),
        BrandsModel(
            pages = listOf(
                contentPage,
                BrandsPage(tab = TabType.MAN),
                BrandsPage(tab = TabType.CHILD)
            ),
            favoriteBrandsText = "1",
            isFavoriteBrandsButtonVisible = true
        )
    )
}
