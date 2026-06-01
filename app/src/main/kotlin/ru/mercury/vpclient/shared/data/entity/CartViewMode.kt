package ru.mercury.vpclient.shared.data.entity

sealed interface CartViewMode {
    data object List: CartViewMode
    data object Cards: CartViewMode
}