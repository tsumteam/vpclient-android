package ru.mercury.vpclient.features.cart_select_size_dialog.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CartSelectSizeDialogIntent: Intent {
    data object SelectSizeClick: CartSelectSizeDialogIntent
    data object DismissRequest: CartSelectSizeDialogIntent
}
