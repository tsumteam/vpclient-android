package ru.mercury.vpclient.features.cart_size_picker_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CartSizePickerSheetIntent: Intent {
    data class SizeClick(val index: Int): CartSizePickerSheetIntent
    data object ConfirmClick: CartSizePickerSheetIntent
    data object DismissRequest: CartSizePickerSheetIntent
}
