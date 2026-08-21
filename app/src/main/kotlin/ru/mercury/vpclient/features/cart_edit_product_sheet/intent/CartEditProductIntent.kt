package ru.mercury.vpclient.features.cart_edit_product_sheet.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CartEditProductIntent: Intent {
    data object DismissClick: CartEditProductIntent
    data class ActionClick(val index: Int): CartEditProductIntent
}
