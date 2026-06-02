package ru.mercury.vpclient.features.cart_color_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CartColorIntent: Intent {
    data class ColorClick(val index: Int): CartColorIntent
    data object ConfirmClick: CartColorIntent
    data object DismissRequest: CartColorIntent
}
