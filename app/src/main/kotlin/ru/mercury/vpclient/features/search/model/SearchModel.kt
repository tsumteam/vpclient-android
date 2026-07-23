package ru.mercury.vpclient.features.search.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.shared.data.entity.SearchSource
import ru.mercury.vpclient.shared.data.entity.TabType
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.mvi.Model

data class SearchModel(
    val query: String = "",
    val source: SearchSource = SearchSource.CATALOG,
    val selectedTab: TabType = TabType.WOMAN,
    val isSelectedTabInitialized: Boolean = false,
    val pages: List<SearchPage> = TabType.entries.map { tab -> SearchPage(tab = tab) },
    val searchSuggestions: List<String> = emptyList(),
    val searchSuggestionsJob: Job? = null,
    val viewHistoryProductEntities: List<CatalogFilterProductsEntity> = emptyList(),
    val viewHistoryJob: Job? = null
): Model {

    val isClearButtonVisible: Boolean
        get() = query.isNotEmpty()

    val isCameraButtonVisible: Boolean
        get() = false

    val isPlaceholderVisible: Boolean
        get() = query.isEmpty()

    val isSearchSuggestionsVisible: Boolean
        get() = query.isNotEmpty() && searchSuggestions.isNotEmpty()

    val isViewHistoryVisible: Boolean
        get() = viewHistoryProductEntities.isNotEmpty()

    val isViewHistoryLoading: Boolean
        get() = viewHistoryJob?.isActive == true
}
