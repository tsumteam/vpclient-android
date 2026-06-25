package ru.mercury.vpclient.features.cart_size_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CartSizeSheetIntent: Intent {
    data object ConfirmClick: CartSizeSheetIntent
    data object DismissRequest: CartSizeSheetIntent
    data object SizeTableClick: CartSizeSheetIntent
    data class SizeClick(val index: Int): CartSizeSheetIntent
}
