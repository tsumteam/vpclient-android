package ru.mercury.vpclient.shared.data.entity

sealed class CartTabsState {
    data object All: CartTabsState()
    data object Payment: CartTabsState()
}