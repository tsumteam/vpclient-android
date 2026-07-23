package ru.mercury.vpclient.features.search

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.activity.event.MainEventManager
import ru.mercury.vpclient.features.details.navigation.DetailsRoute
import ru.mercury.vpclient.features.filter.navigation.FilterRoute
import ru.mercury.vpclient.features.search.event.SearchEvent
import ru.mercury.vpclient.features.search.intent.SearchIntent
import ru.mercury.vpclient.features.search.model.SearchModel
import ru.mercury.vpclient.features.search.navigation.SearchRoute
import ru.mercury.vpclient.shared.data.entity.TabType
import ru.mercury.vpclient.shared.data.network.error.ClientException
import ru.mercury.vpclient.shared.data.network.type.CatalogViewType
import ru.mercury.vpclient.shared.domain.usecase.AddSearchHistoryItemUseCase
import ru.mercury.vpclient.shared.domain.usecase.AddSearchHistoryItemUseCase.Companion.SEARCH_QUERY_MIN_LENGTH
import ru.mercury.vpclient.shared.domain.usecase.CatalogViewHistoryUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogViewHistoryUseCase.CatalogViewHistoryException
import ru.mercury.vpclient.shared.domain.usecase.CatalogViewHistoryUseCase.Companion.SEARCH_VIEW_HISTORY_LIMIT
import ru.mercury.vpclient.shared.domain.usecase.CatalogByTextSuggestsDigineticaUseCase
import ru.mercury.vpclient.shared.domain.usecase.CatalogByTextSuggestsDigineticaUseCase.CatalogByTextSuggestsDigineticaException
import ru.mercury.vpclient.shared.domain.usecase.CatalogByTextSuggestsDigineticaUseCase.Companion.SEARCH_SUGGESTIONS_DEBOUNCE_MILLIS
import ru.mercury.vpclient.shared.domain.usecase.ClearSearchHistoryUseCase
import ru.mercury.vpclient.shared.domain.usecase.RemoveSearchHistoryItemUseCase
import ru.mercury.vpclient.shared.domain.usecase.RemoveSearchHistoryItemUseCase.Params
import ru.mercury.vpclient.shared.domain.usecase.SearchHistoryItemsFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.SelectedTabFlowUseCase
import ru.mercury.vpclient.shared.domain.usecase.SetLastCatalogRootIdUseCase
import ru.mercury.vpclient.shared.domain.usecase.ViewHistoryProductsFlowUseCase
import ru.mercury.vpclient.shared.domain.mapper.catalogRootId
import ru.mercury.vpclient.shared.mvi.ClientViewModel
import ru.mercury.vpclient.shared.navigation.BackRoute
import java.util.UUID
import ru.mercury.vpclient.shared.domain.usecase.AddSearchHistoryItemUseCase.Params as AddSearchHistoryItemParams

@HiltViewModel(assistedFactory = SearchViewModel.Factory::class)
class SearchViewModel @AssistedInject constructor(
    @Assisted private val route: SearchRoute,
    private val catalogViewHistoryUseCase: CatalogViewHistoryUseCase,
    private val catalogByTextSuggestsDigineticaUseCase: CatalogByTextSuggestsDigineticaUseCase,
    private val addSearchHistoryItemUseCase: AddSearchHistoryItemUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase,
    private val removeSearchHistoryItemUseCase: RemoveSearchHistoryItemUseCase,
    private val searchHistoryItemsFlowUseCase: SearchHistoryItemsFlowUseCase,
    private val selectedTabFlowUseCase: SelectedTabFlowUseCase,
    private val setLastCatalogRootIdUseCase: SetLastCatalogRootIdUseCase,
    private val viewHistoryProductsFlowUseCase: ViewHistoryProductsFlowUseCase
): ClientViewModel<SearchIntent, SearchModel, SearchEvent>(SearchModel(source = route.source)) {

    init {
        dispatch(SearchIntent.CollectSearchHistory)
        dispatch(SearchIntent.CollectSelectedTab)
        dispatch(SearchIntent.CollectViewHistoryProducts)
    }

    override fun dispatch(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.CameraClick -> return
            is SearchIntent.CancelClick -> launch { MainEventManager.send(BackRoute) }
            is SearchIntent.ClearClick -> {
                stateFlow.value.searchSuggestionsJob?.cancel()
                reduce {
                    it.copy(
                        query = "",
                        searchSuggestions = emptyList(),
                        searchSuggestionsJob = null
                    )
                }
            }
            is SearchIntent.ClearSearchHistoryClick -> {
                launch { clearSearchHistoryUseCase(intent.tab).getOrThrow() }
            }
            is SearchIntent.CollectSearchHistory -> {
                TabType.entries.forEach { tab ->
                    launch {
                        searchHistoryItemsFlowUseCase(tab)
                            .distinctUntilChanged()
                            .collectLatest { searchHistoryItems ->
                                reduce { state ->
                                    state.copy(
                                        pages = state.pages.map { page ->
                                            when (page.tab) {
                                                tab -> page.copy(
                                                    searchHistoryItems = searchHistoryItems.take(5)
                                                )
                                                else -> page
                                            }
                                        }
                                    )
                                }
                            }
                    }
                }
            }
            is SearchIntent.CollectSelectedTab -> {
                launch {
                    selectedTabFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { tab ->
                            reduce {
                                it.copy(
                                    selectedTab = tab,
                                    isSelectedTabInitialized = true
                                )
                            }
                        }
                }
            }
            is SearchIntent.CollectViewHistoryProducts -> {
                launch {
                    viewHistoryProductsFlowUseCase(Unit)
                        .distinctUntilChanged()
                        .collectLatest { entities ->
                            reduce { it.copy(viewHistoryProductEntities = entities.take(10)) }
                        }
                }
            }
            is SearchIntent.LoadCatalogViewHistory -> {
                val job = launch {
                    catalogViewHistoryUseCase(SEARCH_VIEW_HISTORY_LIMIT).getOrThrow()
                }.also { launchedJob ->
                    launchedJob.invokeOnCompletion {
                        reduce { it.copy(viewHistoryJob = null) }
                    }
                }
                reduce { it.copy(viewHistoryJob = job) }
            }
            is SearchIntent.QueryChange -> {
                stateFlow.value.searchSuggestionsJob?.cancel()
                val searchText = intent.value.trim()
                reduce { state ->
                    when {
                        searchText.isEmpty() -> {
                            state.copy(
                                query = intent.value,
                                searchSuggestions = emptyList(),
                                searchSuggestionsJob = null
                            )
                        }
                        else -> {
                            state.copy(
                                query = intent.value,
                                searchSuggestionsJob = null
                            )
                        }
                    }
                }
                when {
                    searchText.isNotEmpty() -> {
                        val job = launch {
                            delay(SEARCH_SUGGESTIONS_DEBOUNCE_MILLIS)
                            val suggestions = catalogByTextSuggestsDigineticaUseCase(searchText).getOrThrow()
                            reduce { state ->
                                when {
                                    state.query.trim() == searchText -> {
                                        state.copy(searchSuggestions = suggestions)
                                    }
                                    else -> state
                                }
                            }
                        }.also { launchedJob ->
                            launchedJob.invokeOnCompletion {
                                reduce { state ->
                                    when (state.searchSuggestionsJob) {
                                        launchedJob -> state.copy(searchSuggestionsJob = null)
                                        else -> state
                                    }
                                }
                            }
                        }
                        reduce { it.copy(searchSuggestionsJob = job) }
                    }
                }
            }
            is SearchIntent.RemoveSearchHistoryItemClick -> {
                launch { removeSearchHistoryItemUseCase(Params(tab = intent.tab, item = intent.item)).getOrThrow() }
            }
            is SearchIntent.Search -> {
                val query = intent.query.trim()
                if (query.isEmpty()) return
                if (intent.isImeAction && query.length < SEARCH_QUERY_MIN_LENGTH) return
                stateFlow.value.searchSuggestionsJob?.cancel()
                reduce { it.copy(searchSuggestionsJob = null) }
                launch {
                    MainEventManager.send(
                        FilterRoute(
                            categoryId = intent.tab.value,
                            titleCategoryId = intent.tab.value,
                            subtitleCategoryId = intent.tab.value,
                            titleOverride = query,
                            isSingleLineTitle = true,
                            viewTypeOverride = CatalogViewType.TEXT_SEARCH,
                            searchText = query,
                            searchRequestId = UUID.randomUUID().toString(),
                            searchSource = stateFlow.value.source,
                            isMainRoot = true
                        )
                    )
                }
                launch {
                    addSearchHistoryItemUseCase(
                        AddSearchHistoryItemParams(
                            tab = intent.tab,
                            item = query
                        )
                    ).getOrThrow()
                }
            }
            is SearchIntent.SelectTab -> {
                if (!stateFlow.value.isSelectedTabInitialized) return
                reduce { state -> state.copy(selectedTab = intent.tab) }
                launch { setLastCatalogRootIdUseCase(intent.tab.catalogRootId).getOrThrow() }
            }
            is SearchIntent.ViewHistoryProductClick -> {
                launch { MainEventManager.send(DetailsRoute(id = intent.productId, isMainRoot = true)) }
            }
        }
    }

    override fun catch(throwable: Throwable) {
        when (throwable) {
            is CatalogByTextSuggestsDigineticaException -> return
            is CatalogViewHistoryException -> {
                reduce { it.copy(viewHistoryJob = null) }
                launch { send(SearchEvent.SnackbarErrorMessage(throwable.message)) }
            }
            is ClientException -> {
                launch { send(SearchEvent.SnackbarErrorMessage(throwable.message)) }
            }
            else -> super.catch(throwable)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(route: SearchRoute): SearchViewModel
    }
}
