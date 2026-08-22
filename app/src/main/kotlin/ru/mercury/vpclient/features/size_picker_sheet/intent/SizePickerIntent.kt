package ru.mercury.vpclient.features.size_picker_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface SizePickerIntent: Intent {
    data object DismissClick: SizePickerIntent
    data object ConfirmClick: SizePickerIntent
    data object SizeTableClick: SizePickerIntent
    data class SizeClick(val index: Int): SizePickerIntent
}
