package ru.mercury.vpclient.features.details_size_picker_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface DetailsSizePickerSheetIntent: Intent {
    data class SizeClick(val index: Int): DetailsSizePickerSheetIntent
    data object SizeTableClick: DetailsSizePickerSheetIntent
    data object AddToBasketClick: DetailsSizePickerSheetIntent
    data object DismissRequest: DetailsSizePickerSheetIntent
}
