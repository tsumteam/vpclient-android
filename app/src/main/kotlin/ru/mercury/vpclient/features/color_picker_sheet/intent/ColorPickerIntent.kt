package ru.mercury.vpclient.features.color_picker_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface ColorPickerIntent: Intent {
    data object ConfirmClick: ColorPickerIntent
    data object DismissClick: ColorPickerIntent
    data class ColorClick(val index: Int): ColorPickerIntent
}
