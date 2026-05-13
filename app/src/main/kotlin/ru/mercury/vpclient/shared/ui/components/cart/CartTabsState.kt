package ru.mercury.vpclient.shared.ui.components.cart

sealed class CartTabsState {
    data object All: CartTabsState()
    data object Payment: CartTabsState()
}
