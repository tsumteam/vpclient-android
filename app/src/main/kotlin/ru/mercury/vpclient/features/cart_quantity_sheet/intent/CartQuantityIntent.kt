package ru.mercury.vpclient.features.cart_quantity_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CartQuantityIntent: Intent {
    data class QuantityClick(val index: Int): CartQuantityIntent
    data object ConfirmClick: CartQuantityIntent
    data object DismissRequest: CartQuantityIntent
}
