package ru.mercury.vpclient.features.search.intent

import ru.mercury.vpclient.shared.data.entity.TabType
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface SearchIntent: Intent {
    data object CameraClick: SearchIntent
    data object CancelClick: SearchIntent
    data object ClearClick: SearchIntent
    data object CollectSearchHistory: SearchIntent
    data object CollectSelectedTab: SearchIntent
    data object CollectViewHistoryProducts: SearchIntent
    data object LoadCatalogViewHistory: SearchIntent
    data class ClearSearchHistoryClick(val tab: TabType): SearchIntent
    data class QueryChange(val value: String): SearchIntent
    data class RemoveSearchHistoryItemClick(val tab: TabType, val item: String): SearchIntent
    data class Search(val query: String, val tab: TabType, val isImeAction: Boolean = false): SearchIntent
    data class SelectTab(val tab: TabType): SearchIntent
    data class ViewHistoryProductClick(val productId: String): SearchIntent
}
