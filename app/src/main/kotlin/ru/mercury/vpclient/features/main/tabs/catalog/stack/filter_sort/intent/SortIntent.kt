package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_sort.intent

import ru.mercury.vpclient.core.entity.SortType
import ru.mercury.vpclient.core.mvi.Intent

sealed interface SortIntent: Intent {
    data object HideSortDialog: SortIntent
    data class ConfirmSort(val sortType: SortType): SortIntent
}
