package ru.mercury.vpclient.features.search.model

import ru.mercury.vpclient.shared.data.entity.TabType

data class SearchPage(
    val tab: TabType,
    val searchHistoryItems: List<String> = emptyList()
) {
    val isSearchHistoryVisible: Boolean
        get() = searchHistoryItems.isNotEmpty()
}
