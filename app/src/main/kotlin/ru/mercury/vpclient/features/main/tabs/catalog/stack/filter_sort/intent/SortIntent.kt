package ru.mercury.vpclient.features.main.tabs.catalog.stack.filter_sort.intent

import ru.mercury.vpclient.shared.data.entity.SortType
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface SortIntent: Intent {
    data object HideSortDialog: SortIntent
    data class ConfirmSort(val sortType: SortType): SortIntent
}
