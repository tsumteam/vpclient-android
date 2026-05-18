package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.features.cart.model.CartViewMode
import ru.mercury.vpclient.shared.ui.components.cart.CartTabsState
import ru.mercury.vpclient.shared.ui.components.cart.CartToolbarState

class CartToolbarStateProvider: PreviewParameterProvider<CartToolbarState> {
    override val values: Sequence<CartToolbarState> = sequenceOf(
        CartToolbarState(
            tabsState = CartTabsState.All,
            viewMode = CartViewMode.List,
            isViewModeSwitcherVisible = false,
            allItemsCount = 100,
            paymentItemsCount = 2,
            onAllClick = {},
            onPaymentClick = {},
            onCardsClick = {},
            onListClick = {}
        ),
        CartToolbarState(
            tabsState = CartTabsState.Payment,
            viewMode = CartViewMode.List,
            isViewModeSwitcherVisible = false,
            allItemsCount = 100,
            paymentItemsCount = 2,
            onAllClick = {},
            onPaymentClick = {},
            onCardsClick = {},
            onListClick = {}
        ),
        CartToolbarState(
            tabsState = CartTabsState.All,
            viewMode = CartViewMode.List,
            isViewModeSwitcherVisible = true,
            allItemsCount = 100,
            paymentItemsCount = 2,
            onAllClick = {},
            onPaymentClick = {},
            onCardsClick = {},
            onListClick = {}
        )
    )
}
