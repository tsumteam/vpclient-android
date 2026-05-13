package ru.mercury.vpclient.features.cart.model

sealed interface CartViewMode {
    data object List: CartViewMode
    data object Cards: CartViewMode
}
