package ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.mercury.vpclient.core.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.core.ui.components.catalog.CatalogClothingCard
import ru.mercury.vpclient.core.ui.components.catalog.CatalogTabRow
import ru.mercury.vpclient.core.ui.components.IndicatorIconButton
import ru.mercury.vpclient.core.ui.components.system.ClientCenterAlignedTopAppBar
import ru.mercury.vpclient.core.ui.components.system.ClientLazyColumn
import ru.mercury.vpclient.core.ui.components.system.ClientSnackbarHost
import ru.mercury.vpclient.core.ui.icons.Basket24
import ru.mercury.vpclient.core.ui.icons.Chat24
import ru.mercury.vpclient.core.ui.icons.FittingShirt24
import ru.mercury.vpclient.core.ui.icons.Search24
import ru.mercury.vpclient.core.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.core.ui.preview.CatalogModelProvider
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.medium17
import ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.event.CatalogEvent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.intent.CatalogIntent
import ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.model.CatalogModel
import ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.ui.CatalogClothingContent

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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                ClientCenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(ClientStrings.MainTabCatalog),
                            style = MaterialTheme.typography.medium17.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center
                            )
                        )
                    },
                    navigationIcon = {
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

                CatalogTabRow(
                    tabs = state.catalogData.tabs,
                    selectedTabIndex = pagerState.currentPage,
                    onTabClick = { index -> scope.launch { pagerState.animateScrollToPage(index) } }
                )
            }
        },
        snackbarHost = {
            ClientSnackbarHost(
                hostState = snackbarHostStateError,
                modifier = Modifier.padding(horizontal = 16.dp),
                containerColor = MaterialTheme.colorScheme.error
            )
        }
    ) { innerPadding ->
        when {
            state.isLoading -> {
                ClientLazyColumn(
                    modifier = Modifier
                        .padding(top = innerPadding.calculateTopPadding())
                        .fillMaxSize(),
                    contentPadding = PaddingValues(top = 8.dp),
                    userScrollEnabled = false
                ) {
                    items(
                        count = 6,
                        key = { index -> "catalog_placeholder_$index" }
                    ) { index ->
                        CatalogClothingCard(
                            entity = CatalogCategoryEntity.Empty
                        )

                        if (index != 6) {
                            Spacer(
                                modifier = Modifier.height(8.dp)
                            )
                        }
                    }
                }
            }
            else -> {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .padding(top = innerPadding.calculateTopPadding())
                        .fillMaxSize(),
                    pageSpacing = 8.dp
                ) { page ->
                    val pageData = state.catalogData.pages.getOrNull(page)

                    CatalogClothingContent(
                        entities = pageData.orEmpty(),
                        onItemClick = { dispatch(CatalogIntent.CategoryClick(it.id)) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun CatalogScreenContentPreview(
    @PreviewParameter(CatalogModelProvider::class) state: CatalogModel
) {
    ClientTheme {
        CatalogScreenContent(
            state = state,
            dispatch = {},
            snackbarHostStateError = remember { SnackbarHostState() }
        )
    }
}
