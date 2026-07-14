@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.home.event.HomeEvent
import ru.mercury.vpclient.features.home.intent.HomeIntent
import ru.mercury.vpclient.features.home.model.HomeModel
import ru.mercury.vpclient.shared.data.entity.HomePage
import ru.mercury.vpclient.shared.data.entity.HomeSectionEntity
import ru.mercury.vpclient.shared.data.entity.HomeSectionItemEntity
import ru.mercury.vpclient.shared.data.entity.HomeSectionType
import ru.mercury.vpclient.shared.data.entity.TabType
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.NotificationIconButton
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedPullToRefreshBox
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.cart.CartIconButton
import ru.mercury.vpclient.shared.ui.components.cart.FittingIconButton
import ru.mercury.vpclient.shared.ui.components.cart.MessengerIconButton
import ru.mercury.vpclient.shared.ui.components.catalog.CatalogTabData
import ru.mercury.vpclient.shared.ui.components.catalog.TabRow
import ru.mercury.vpclient.shared.ui.components.home.HomeBannerCarousel
import ru.mercury.vpclient.shared.ui.components.home.HomeBannerCarouselState
import ru.mercury.vpclient.shared.ui.components.home.HomeGiftCards
import ru.mercury.vpclient.shared.ui.components.home.HomeGiftCardsState
import ru.mercury.vpclient.shared.ui.components.home.HomeProductsCarousel
import ru.mercury.vpclient.shared.ui.components.home.HomeProductsCarouselState
import ru.mercury.vpclient.shared.ui.components.home.HomeSmallBannersCarousel
import ru.mercury.vpclient.shared.ui.components.home.HomeSmallBannersCarouselState
import ru.mercury.vpclient.shared.ui.components.home.HomeVideoCarousel
import ru.mercury.vpclient.shared.ui.components.home.HomeVideoCarouselState
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.icons.Logo82
import ru.mercury.vpclient.shared.ui.icons.Search24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientStrings

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostStateError = remember { SnackbarHostState() }

    HomeScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        snackbarHostStateError = snackbarHostStateError
    )

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is HomeEvent.SnackbarErrorMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
        }
    }
}

@Composable
private fun HomeScreenContent(
    state: HomeModel,
    dispatch: (HomeIntent) -> Unit,
    snackbarHostStateError: SnackbarHostState
) {
    val selectedTabIndex = TabType.entries.indexOf(state.selectedTab)
    val pagerState = rememberPagerState(
        initialPage = selectedTabIndex.coerceAtLeast(0),
        pageCount = { TabType.entries.size }
    )
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
                    ?.let { tab -> dispatch(HomeIntent.SelectTab(tab)) }
            }
    }

    SharedScaffold(
        topBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Icon(
                            imageVector = Logo82,
                            contentDescription = null,
                            modifier = Modifier.size(82.dp, 57.dp)
                        )
                    },
                    navigationIcon = {
                        Row {
                            IconButton(
                                onClick = { dispatch(HomeIntent.SearchClick) },
                                modifier = Modifier.size(42.dp)
                            ) {
                                Icon(
                                    imageVector = Search24,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            NotificationIconButton(
                                showBadge = state.isNotificationBadgeVisible,
                                onClick = { dispatch(HomeIntent.NotificationClick) }
                            )
                        }
                    },
                    actions = {
                        if (state.isFittingButtonVisible) {
                            FittingIconButton(
                                text = state.fittingText,
                                showBadge = state.isFittingBadgeVisible,
                                onClick = { dispatch(HomeIntent.FittingClick) }
                            )
                        }

                        CartIconButton(
                            text = state.cartText,
                            showBadge = state.isCartBadgeVisible,
                            onClick = { dispatch(HomeIntent.CartClick) }
                        )

                        MessengerIconButton(
                            showBadge = state.isMessengerBadgeVisible,
                            onClick = { dispatch(HomeIntent.MessengerClick) }
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
                    onTabClick = { index ->
                        scope.launch { pagerState.animateScrollToPage(index) }
                    }
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
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = innerPadding.calculateTopPadding()),
            pageSpacing = 8.dp
        ) { page ->
            val pageData = state.pages[page]

            SharedPullToRefreshBox(
                isRefreshing = pageData.tab in state.loadedTabs &&
                    pageData.tab in state.loadMainScreenSectionsJobs,
                onRefresh = { dispatch(HomeIntent.LoadMainScreenSections(pageData.tab)) },
                modifier = Modifier.fillMaxSize()
            ) {
                SharedLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    userScrollEnabled = pageData.tab in state.loadedTabs
                ) {
                    when {
                        pageData.tab !in state.loadedTabs -> {
                            item {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1627F / 1152F)
                                        .placeholder(
                                            visible = true,
                                            highlight = PlaceholderHighlight.shimmer(),
                                            color = MaterialTheme.colorScheme.surfaceVariant
                                        )
                                )
                            }
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Spacer(
                                        modifier = Modifier
                                            .size(width = 200.dp, height = 24.dp)
                                            .placeholder(
                                                visible = true,
                                                highlight = PlaceholderHighlight.shimmer(),
                                                color = MaterialTheme.colorScheme.surfaceVariant,
                                                shape = RoundedCornerShape(4.dp)
                                            )
                                    )
                                }
                            }
                            item {
                                LazyRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(266.dp),
                                    contentPadding = PaddingValues(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    items(
                                        count = 3
                                    ) {
                                        Spacer(
                                            modifier = Modifier
                                                .size(width = 148.dp, height = 266.dp)
                                                .clip(RoundedCornerShape(4.dp))
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
                            item {
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(1627F / 1152F)
                                        .placeholder(
                                            visible = true,
                                            highlight = PlaceholderHighlight.shimmer(),
                                            color = MaterialTheme.colorScheme.surfaceVariant
                                        )
                                )
                            }
                        }
                        else -> {
                            items(
                                items = pageData.homeSectionEntities,
                                key = { section -> section.order }
                            ) { section ->
                                when (section.type) {
                                    HomeSectionType.BIG_BANNERS_WITHOUT_TITLE_CAROUSEL,
                                    HomeSectionType.BIG_BANNERS_WITH_TITLE_CAROUSEL,
                                    HomeSectionType.MEDIUM_BANNERS_WITHOUT_TITLE_CAROUSEL -> {
                                        HomeBannerCarousel(
                                            state = HomeBannerCarouselState(
                                                section = section,
                                                onItemClick = { item ->
                                                    dispatch(HomeIntent.SectionItemClick(item))
                                                }
                                            )
                                        )
                                    }
                                    HomeSectionType.SMALL_BANNERS_CAROUSEL -> {
                                        HomeSmallBannersCarousel(
                                            state = HomeSmallBannersCarouselState(
                                                section = section,
                                                onItemClick = { item ->
                                                    dispatch(HomeIntent.SectionItemClick(item))
                                                },
                                                onViewMoreClick = {
                                                    dispatch(HomeIntent.SectionViewMoreClick(section))
                                                }
                                            )
                                        )
                                    }
                                    HomeSectionType.PRODUCTS_CAROUSEL -> {
                                        HomeProductsCarousel(
                                            state = HomeProductsCarouselState(
                                                section = section,
                                                onProductClick = { item ->
                                                    dispatch(HomeIntent.ProductClick(item))
                                                },
                                                onViewMoreClick = {
                                                    dispatch(HomeIntent.SectionViewMoreClick(section))
                                                }
                                            )
                                        )
                                    }
                                    HomeSectionType.CATALOG_COMPILATIONS -> {
                                        ClientAsyncImage(
                                            imageUrl = section.imageUrl,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .aspectRatio(1F)
                                        )
                                    }
                                    HomeSectionType.GIFT_CARDS -> {
                                        HomeGiftCards(
                                            state = HomeGiftCardsState(
                                                section = section
                                            )
                                        )
                                    }
                                    HomeSectionType.SQUARE_VIDEOS_WITHOUT_TITLE_CAROUSEL,
                                    HomeSectionType.VERTICAL_VIDEOS_WITHOUT_TITLE_CAROUSEL,
                                    HomeSectionType.SQUARE_VIDEOS_WITH_TITLE_CAROUSEL,
                                    HomeSectionType.VERTICAL_VIDEOS_WITH_TITLE_CAROUSEL -> {
                                        HomeVideoCarousel(
                                            state = HomeVideoCarouselState(
                                                section = section
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(heightDp = 1100)
@Composable
private fun HomeScreenContentPreview(
    @PreviewParameter(HomeModelPreviewParameterProvider::class) state: HomeModel
) {
    HomeScreenContent(
        state = state,
        dispatch = {},
        snackbarHostStateError = remember { SnackbarHostState() }
    )
}

private class HomeModelPreviewParameterProvider: PreviewParameterProvider<HomeModel> {
    override val values: Sequence<HomeModel> = sequenceOf(
        HomeModel(),
        HomeModel(
            pages = TabType.entries.map { tab ->
                HomePage(
                    tab = tab,
                    homeSectionEntities = when (tab) {
                        TabType.WOMAN -> listOf(
                            HomeSectionEntity(
                                type = HomeSectionType.MEDIUM_BANNERS_WITHOUT_TITLE_CAROUSEL,
                                order = 1,
                                items = listOf(
                                    HomeSectionItemEntity(),
                                    HomeSectionItemEntity()
                                )
                            ),
                            HomeSectionEntity(
                                type = HomeSectionType.SMALL_BANNERS_CAROUSEL,
                                order = 2,
                                title = "ПОПУЛЯРНЫЕ КАТЕГОРИИ",
                                items = listOf(
                                    HomeSectionItemEntity(title = "НОВИНКИ"),
                                    HomeSectionItemEntity(title = "ОДЕЖДА"),
                                    HomeSectionItemEntity(title = "ОБУВЬ")
                                )
                            ),
                            HomeSectionEntity(
                                type = HomeSectionType.PRODUCTS_CAROUSEL,
                                order = 3,
                                title = "РЕКОМЕНДУЕМ",
                                items = listOf(
                                    HomeSectionItemEntity(
                                        title = "ПЛАТЬЕ ИЗ ШЕЛКА",
                                        brand = "DOLCE & GABBANA",
                                        productId = "preview-product-1"
                                    ),
                                    HomeSectionItemEntity(
                                        title = "ЖАКЕТ ИЗ ШЕРСТИ",
                                        brand = "SAINT LAURENT",
                                        productId = "preview-product-2"
                                    )
                                )
                            )
                        )
                        else -> emptyList()
                    }
                )
            },
            loadedTabs = setOf(TabType.WOMAN)
        )
    )
}
