@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
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
import ru.mercury.vpclient.features.catalog.event.CatalogEvent
import ru.mercury.vpclient.features.catalog.intent.CatalogIntent
import ru.mercury.vpclient.features.catalog.model.CatalogModel
import ru.mercury.vpclient.shared.data.entity.CatalogTabData
import ru.mercury.vpclient.shared.data.network.type.CatalogCategoryType
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.domain.usecase.CatalogDataFlowUseCase.CatalogData
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.cart.CartIconButton
import ru.mercury.vpclient.shared.ui.components.cart.FittingIconButton
import ru.mercury.vpclient.shared.ui.components.cart.MessengerIconButton
import ru.mercury.vpclient.shared.ui.components.catalog.CatalogClothingCard
import ru.mercury.vpclient.shared.ui.components.catalog.CatalogTabRow
import ru.mercury.vpclient.shared.ui.icons.Search24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium18

@Composable
fun CatalogScreen(
    viewModel: CatalogViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostStateError = remember { SnackbarHostState() }

    CatalogScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        snackbarHostStateError = snackbarHostStateError
    )

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is CatalogEvent.SnackbarMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
        }
    }
}

@Composable
private fun CatalogScreenContent(
    state: CatalogModel,
    dispatch: (CatalogIntent) -> Unit,
    snackbarHostStateError: SnackbarHostState
) {
    val pagerState = rememberPagerState(pageCount = { state.catalogData.pages.size.coerceAtLeast(1) })
    val scope = rememberCoroutineScope()
    var selectionInitialized by remember(state.catalogData.tabs) { mutableStateOf(false) }

    LaunchedEffect(state.selectedTabIndex, state.catalogData.tabs) {
        if (state.catalogData.tabs.getOrNull(state.selectedTabIndex) != null && pagerState.currentPage != state.selectedTabIndex) {
            pagerState.scrollToPage(state.selectedTabIndex)
        }
        selectionInitialized = state.catalogData.tabs.getOrNull(state.selectedTabIndex) != null
    }

    LaunchedEffect(pagerState, state.catalogData.tabs, selectionInitialized) {
        if (selectionInitialized) {
            snapshotFlow { pagerState.currentPage }
                .distinctUntilChanged()
                .collect { page ->
                    if (state.catalogData.tabs.getOrNull(page) != null) {
                        dispatch(CatalogIntent.SelectTab(page))
                    }
                }
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
                            text = stringResource(ClientStrings.MainTabCatalog),
                            style = MaterialTheme.typography.medium18
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                // fixme
                            },
                            modifier = Modifier.size(42.dp)
                        ) {
                            Icon(
                                imageVector = Search24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    actions = {
                        if (state.isFittingButtonVisible) {
                            FittingIconButton(
                                text = state.fittingText,
                                showBadge = state.isFittingBadgeVisible,
                                onClick = { dispatch(CatalogIntent.FittingClick) }
                            )
                        }

                        CartIconButton(
                            text = state.cartText,
                            showBadge = state.isCartBadgeVisible,
                            onClick = { dispatch(CatalogIntent.CartClick) }
                        )

                        MessengerIconButton(
                            showBadge = state.isMessengerBadgeVisible,
                            onClick = { dispatch(CatalogIntent.MessengerClick) }
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors().copy(
                        containerColor = MaterialTheme.colorScheme.background,
                        navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                        titleContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )

                CatalogTabRow(
                    tabs = state.catalogData.tabs,
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
        when {
            state.isLoading -> {
                SharedLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding + PaddingValues(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    userScrollEnabled = false
                ) {
                    items(
                        count = 6
                    ) {
                        CatalogClothingCard(
                            entity = CatalogCategoryEntity.Empty
                        )
                    }
                }
            }
            else -> {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = innerPadding.calculateTopPadding()),
                    pageSpacing = 8.dp
                ) { page ->
                    val pageData = state.catalogData.pages.getOrNull(page)
                    val entities = pageData.orEmpty()

                    SharedLazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 16.dp, bottom = 8.dp)
                    ) {
                        itemsIndexed(
                            items = entities,
                            key = { _, entity -> entity.id }
                        ) { index, entity ->
                            CatalogClothingCard(
                                entity = entity,
                                modifier = Modifier.clickable {
                                    dispatch(CatalogIntent.CategoryClick(entity))
                                }
                            )

                            if (index != entities.lastIndex) {
                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )
                            }
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
private fun CatalogScreenContentPreview(
    @PreviewParameter(CatalogModelProvider::class) state: CatalogModel
) {
    CatalogScreenContent(
        state = state,
        dispatch = {},
        snackbarHostStateError = remember { SnackbarHostState() }
    )
}

private class CatalogModelProvider: PreviewParameterProvider<CatalogModel> {
    private val clothesCategory = CatalogCategoryEntity(
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

    override val values: Sequence<CatalogModel> = sequenceOf(
        CatalogModel(),
        CatalogModel(
            catalogData = CatalogData(
                tabs = listOf(
                    CatalogTabData(title = "Мужская", rootId = 3, selected = true),
                    CatalogTabData(title = "Женская", rootId = 2, selected = false),
                    CatalogTabData(title = "Детская", rootId = 4, selected = false)
                ),
                pages = listOf(
                    listOf(
                        clothesCategory,
                        clothesCategory.copy(
                            id = 11,
                            parentId = 3,
                            rootId = 3,
                            name = "Аксессуары",
                            position = 2
                        )
                    )
                )
            )
        )
    )
}
