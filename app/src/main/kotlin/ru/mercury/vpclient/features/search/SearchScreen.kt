package ru.mercury.vpclient.features.search

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.search.event.SearchEvent
import ru.mercury.vpclient.features.search.intent.SearchIntent
import ru.mercury.vpclient.features.search.model.SearchModel
import ru.mercury.vpclient.features.search.model.SearchPage
import ru.mercury.vpclient.features.search.navigation.SearchRoute
import ru.mercury.vpclient.shared.data.entity.TabType
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.catalog.CatalogTabData
import ru.mercury.vpclient.shared.ui.components.catalog.TabRow
import ru.mercury.vpclient.shared.ui.components.search.SearchHistoryHeader
import ru.mercury.vpclient.shared.ui.components.search.SearchHistoryHeaderState
import ru.mercury.vpclient.shared.ui.components.search.SearchHistoryItem
import ru.mercury.vpclient.shared.ui.components.search.SearchHistoryItemState
import ru.mercury.vpclient.shared.ui.components.search.SearchSuggestionItem
import ru.mercury.vpclient.shared.ui.components.search.SearchSuggestionItemState
import ru.mercury.vpclient.shared.ui.components.search.SearchViewHistoryHeader
import ru.mercury.vpclient.shared.ui.components.search.SearchViewHistoryHeaderState
import ru.mercury.vpclient.shared.ui.components.search.SearchViewHistoryRow
import ru.mercury.vpclient.shared.ui.components.search.SearchViewHistoryRowState
import ru.mercury.vpclient.shared.ui.icons.Camera24
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.icons.Search24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular16

@Composable
fun SearchScreen(
    route: SearchRoute,
    viewModel: SearchViewModel = hiltViewModel<SearchViewModel, SearchViewModel.Factory>(
        creationCallback = { it.create(route) }
    )
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }
    val snackbarHostStateError = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    SearchScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        focusRequester = focusRequester,
        snackbarHostStateError = snackbarHostStateError
    )

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.dispatch(SearchIntent.LoadCatalogViewHistory)
    }

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is SearchEvent.SnackbarErrorMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
        }
    }
}

@Composable
private fun SearchScreenContent(
    state: SearchModel,
    dispatch: (SearchIntent) -> Unit,
    focusRequester: FocusRequester,
    snackbarHostStateError: SnackbarHostState
) {
    val selectedTabIndex = TabType.entries.indexOf(state.selectedTab)
    val pagerState = rememberPagerState(
        initialPage = selectedTabIndex.coerceAtLeast(0),
        pageCount = { state.pages.size }
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
    var queryTextFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = state.query,
                selection = TextRange(index = state.query.length)
            )
        )
    }

    LaunchedEffect(state.query) {
        when {
            queryTextFieldValue.text != state.query -> {
                queryTextFieldValue = TextFieldValue(
                    text = state.query,
                    selection = TextRange(index = state.query.length)
                )
            }
        }
    }

    LaunchedEffect(selectedTabIndex) {
        when {
            selectedTabIndex in state.pages.indices && pagerState.currentPage != selectedTabIndex -> {
                pagerState.scrollToPage(selectedTabIndex)
            }
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { page ->
                state.pages
                    .getOrNull(page)
                    ?.let { searchPage -> dispatch(SearchIntent.SelectTab(searchPage.tab)) }
            }
    }

    SharedScaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .statusBarsPadding()
                    .padding(top = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1F)
                            .height(52.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clip(RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Icon(
                            imageVector = Search24,
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 16.dp)
                                .size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        BasicTextField(
                            value = queryTextFieldValue,
                            onValueChange = { value ->
                                queryTextFieldValue = value
                                when {
                                    value.text != state.query -> {
                                        dispatch(SearchIntent.QueryChange(value.text))
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 48.dp,
                                    end = 48.dp
                                )
                                .focusRequester(focusRequester)
                                .onFocusChanged { focusState ->
                                    when {
                                        focusState.isFocused -> {
                                            queryTextFieldValue = queryTextFieldValue.copy(
                                                selection = TextRange(index = queryTextFieldValue.text.length)
                                            )
                                        }
                                    }
                                },
                            singleLine = true,
                            textStyle = MaterialTheme.typography.regular16.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                lineHeight = 20.sp,
                                letterSpacing = .2.sp
                            ),
                            cursorBrush = SolidColor(MaterialTheme.colorScheme.error),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    dispatch(
                                        SearchIntent.Search(
                                            query = state.query,
                                            tab = state.selectedTab,
                                            isImeAction = true
                                        )
                                    )
                                }
                            ),
                            decorationBox = { innerTextField ->
                                Box(
                                    contentAlignment = Alignment.CenterStart
                                ) {
                                    when {
                                        state.isPlaceholderVisible -> {
                                            Text(
                                                text = stringResource(ClientStrings.SearchPlaceholder),
                                                style = MaterialTheme.typography.regular16.copy(
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    lineHeight = 20.sp,
                                                    letterSpacing = .2.sp
                                                )
                                            )
                                        }
                                    }

                                    innerTextField()
                                }
                            }
                        )

                        when {
                            state.isClearButtonVisible -> {
                                IconButton(
                                    onClick = { dispatch(SearchIntent.ClearClick) },
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                ) {
                                    Icon(
                                        imageVector = Close24,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                        tint = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                            state.isCameraButtonVisible -> {
                                IconButton(
                                    onClick = { dispatch(SearchIntent.CameraClick) },
                                    modifier = Modifier.align(Alignment.CenterEnd)
                                ) {
                                    Icon(
                                        imageVector = Camera24,
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    TextButton(
                        onClick = { dispatch(SearchIntent.CancelClick) },
                        modifier = Modifier.height(52.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(
                            text = stringResource(ClientStrings.SearchCancel),
                            style = MaterialTheme.typography.medium15.copy(
                                lineHeight = 15.sp,
                                letterSpacing = .3.sp
                            )
                        )
                    }
                }

                TabRow(
                    tabs = tabs,
                    selectedTabIndex = selectedTabIndex,
                    onTabClick = { index ->
                        scope.launch { pagerState.animateScrollToPage(index) }
                    },
                    modifier = Modifier.padding(top = 8.dp)
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
            state.isSearchSuggestionsVisible -> {
                SharedLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = innerPadding.calculateTopPadding(),
                        bottom = innerPadding.calculateBottomPadding() + 16.dp
                    )
                ) {
                    items(
                        items = state.searchSuggestions
                    ) { suggestion ->
                        SearchSuggestionItem(
                            state = SearchSuggestionItemState(
                                text = suggestion,
                                query = state.query.trim(),
                                onClick = {
                                    dispatch(
                                        SearchIntent.Search(
                                            query = suggestion,
                                            tab = state.selectedTab
                                        )
                                    )
                                }
                            )
                        )
                    }
                }
            }
            else -> {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        top = innerPadding.calculateTopPadding()
                    ),
                    pageSpacing = 8.dp
                ) { page ->
                    val pageData = state.pages[page]

                    SharedLazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = 16.dp,
                            bottom = innerPadding.calculateBottomPadding() + 16.dp
                        )
                    ) {
                        if (pageData.isSearchHistoryVisible) {
                            item {
                                SearchHistoryHeader(
                                    state = SearchHistoryHeaderState(
                                        title = stringResource(ClientStrings.SearchRecentlySearchedCaps),
                                        clearButtonText = stringResource(ClientStrings.SearchClear),
                                        onClearClick = {
                                            dispatch(SearchIntent.ClearSearchHistoryClick(pageData.tab))
                                        }
                                    )
                                )
                            }
                            items(
                                items = pageData.searchHistoryItems,
                                key = { item -> item }
                            ) { item ->
                                SearchHistoryItem(
                                    state = SearchHistoryItemState(
                                        text = item,
                                        onClick = {
                                            dispatch(
                                                SearchIntent.Search(
                                                    query = item,
                                                    tab = pageData.tab
                                                )
                                            )
                                        },
                                        onClearClick = {
                                            dispatch(
                                                SearchIntent.RemoveSearchHistoryItemClick(
                                                    tab = pageData.tab,
                                                    item = item
                                                )
                                            )
                                        }
                                    )
                                )
                            }
                        }
                        if (pageData.isSearchHistoryVisible && state.isViewHistoryVisible) {
                            item {
                                Spacer(
                                    modifier = Modifier.height(24.dp)
                                )
                            }
                        }
                        if (state.isViewHistoryVisible) {
                            item {
                                SearchViewHistoryHeader(
                                    state = SearchViewHistoryHeaderState(
                                        title = stringResource(ClientStrings.SearchRecentlyViewedCaps)
                                    )
                                )
                            }
                            item {
                                SearchViewHistoryRow(
                                    state = SearchViewHistoryRowState(
                                        productEntities = state.viewHistoryProductEntities,
                                        onProductClick = { entity ->
                                            dispatch(SearchIntent.ViewHistoryProductClick(entity.id))
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
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun SearchScreenContentPreview(
    @PreviewParameter(SearchModelPreviewParameterProvider::class) state: SearchModel
) {
    SearchScreenContent(
        state = state,
        dispatch = {},
        focusRequester = remember { FocusRequester() },
        snackbarHostStateError = remember { SnackbarHostState() }
    )
}

private class SearchModelPreviewParameterProvider: PreviewParameterProvider<SearchModel> {
    override val values: Sequence<SearchModel> = sequenceOf(
        SearchModel(),
        SearchModel(
            query = "жилет",
            searchSuggestions = listOf(
                "Жилет из шерсти",
                "Удлиненный жилет",
                "Трикотажный жилет"
            )
        ),
        SearchModel(
            pages = TabType.entries.map { tab ->
                SearchPage(
                    tab = tab,
                    searchHistoryItems = when (tab) {
                        TabType.WOMAN -> listOf(
                            "акция",
                            "сувениры"
                        )
                        TabType.MAN -> listOf(
                            "жилет"
                        )
                        TabType.CHILD -> emptyList()
                    }
                )
            },
            viewHistoryProductEntities = listOf(
                CatalogFilterProductsEntity.Empty.copy(
                    id = "1",
                    brand = "BALMAIN",
                    imageUrl = ""
                ),
                CatalogFilterProductsEntity.Empty.copy(
                    id = "2",
                    brand = "DOLCE&GABBANA",
                    imageUrl = ""
                ),
                CatalogFilterProductsEntity.Empty.copy(
                    id = "3",
                    brand = "MVST",
                    imageUrl = ""
                )
            )
        )
    )
}
