package ru.mercury.vpclient.features.cart_empty_order_dialog.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CartEmptyOrderDialogIntent: Intent {
    data object DismissRequest: CartEmptyOrderDialogIntent
}
