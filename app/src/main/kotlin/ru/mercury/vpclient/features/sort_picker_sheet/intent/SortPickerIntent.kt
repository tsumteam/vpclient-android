package ru.mercury.vpclient.features.sort_picker_sheet.intent

import ru.mercury.vpclient.shared.data.entity.SortType
import ru.mercury.vpclient.shared.mvi.Intent

sealed interface SortPickerIntent: Intent {
    data object DismissClick: SortPickerIntent
    data class ConfirmSort(val sortType: SortType): SortPickerIntent
}
