package ru.mercury.vpclient.features.cart_fitting_color_picker_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CartFittingColorPickerSheetIntent: Intent {
    data class ColorClick(val index: Int): CartFittingColorPickerSheetIntent
    data object ConfirmClick: CartFittingColorPickerSheetIntent
    data object DismissRequest: CartFittingColorPickerSheetIntent
}
