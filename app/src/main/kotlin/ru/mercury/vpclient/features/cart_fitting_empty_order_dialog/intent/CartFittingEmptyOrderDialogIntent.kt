package ru.mercury.vpclient.features.cart_fitting_empty_order_dialog.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CartFittingEmptyOrderDialogIntent: Intent {
    data object DismissRequest: CartFittingEmptyOrderDialogIntent
}
