package ru.mercury.vpclient.shared.ui.components.cart

import ru.mercury.vpclient.features.cart.model.CartViewMode

data class CartToolbarState(
    val tabsState: CartTabsState,
    val viewMode: CartViewMode,
    val isViewModeSwitcherVisible: Boolean,
    val allItemsCount: Int,
    val paymentItemsCount: Int,
    val onAllClick: () -> Unit,
    val onPaymentClick: () -> Unit,
    val onCardsClick: () -> Unit,
    val onListClick: () -> Unit
)
