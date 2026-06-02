package ru.mercury.vpclient.features.cart_size_require_dialog.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CartSizeRequireIntent: Intent {
    data object SelectSizeClick: CartSizeRequireIntent
    data object DismissRequest: CartSizeRequireIntent
}
