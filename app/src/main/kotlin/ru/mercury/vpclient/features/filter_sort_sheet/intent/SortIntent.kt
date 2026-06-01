package ru.mercury.vpclient.features.filter_sort_sheet.intent

import ru.mercury.vpclient.shared.data.entity.SortType
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface SortIntent: Intent {
    data object HideSortDialog: SortIntent
    data class ConfirmSort(val sortType: SortType): SortIntent
}
