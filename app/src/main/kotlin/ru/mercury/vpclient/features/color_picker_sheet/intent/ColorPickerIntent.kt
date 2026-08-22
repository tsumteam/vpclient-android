package ru.mercury.vpclient.features.color_picker_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ColorPickerIntent: Intent {
    data object DismissClick: ColorPickerIntent
    data object ConfirmClick: ColorPickerIntent
    data class ColorClick(val index: Int): ColorPickerIntent
}
