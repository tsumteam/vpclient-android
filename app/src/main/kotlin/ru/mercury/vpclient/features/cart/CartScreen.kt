@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.mercury.vpclient.features.cart.intent.CartIntent
import ru.mercury.vpclient.features.cart.model.CartModel
import ru.mercury.vpclient.features.cart.model.CartPayMode
import ru.mercury.vpclient.features.cart.model.CartViewMode
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.ui.components.cart.CartBottomBar
import ru.mercury.vpclient.shared.ui.components.cart.CartLargeProductCard
import ru.mercury.vpclient.shared.ui.components.cart.CartListLoading
import ru.mercury.vpclient.shared.ui.components.cart.CartListProductCard
import ru.mercury.vpclient.shared.ui.components.cart.CartSelectSizeDialog
import ru.mercury.vpclient.shared.ui.components.cart.CartSizePickerSheet
import ru.mercury.vpclient.shared.ui.components.cart.CartSummary
import ru.mercury.vpclient.shared.ui.components.cart.CartTabsState
import ru.mercury.vpclient.shared.ui.components.cart.CartToolbar
import ru.mercury.vpclient.shared.ui.components.cart.CartToolbarLoading
import ru.mercury.vpclient.shared.ui.components.system.ClientLazyColumn
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.CartProductProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium18

@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    CartScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun CartScreenContent(
    state: CartModel,
    dispatch: (CartIntent) -> Unit
) {
    state.selectSizeProduct?.let { product ->
        CartSelectSizeDialog(
            onSelectSizeClick = { dispatch(CartIntent.SelectSizeClick(product)) },
            onDismissRequest = { dispatch(CartIntent.HideSelectSizeDialog) }
        )
    }

    if (state.sizePickerSizes != null) {
        CartSizePickerSheet(
            state = state.sizePickerState,
            onSizeClick = { index -> dispatch(CartIntent.ToggleSizePickerItem(index)) },
            onConfirmClick = { dispatch(CartIntent.ConfirmSizePicker) },
            onDismissRequest = { dispatch(CartIntent.HideSizePicker) }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(ClientStrings.CartTitle),
                            style = MaterialTheme.typography.medium18.copy(
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { dispatch(CartIntent.CloseClick) }
                        ) {
                            Icon(
                                imageVector = Close24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )

                when {
                    state.products.isEmpty() -> CartToolbarLoading()
                    else -> {
                        CartToolbar(
                            tabsState = when (state.payMode) {
                                CartPayMode.All -> CartTabsState.All
                                CartPayMode.Payment -> CartTabsState.Payment
                            },
                            viewMode = state.viewMode,
                            allItemsCount = state.allItemsCount,
                            paymentItemsCount = state.paymentItemsCount,
                            onAllClick = { dispatch(CartIntent.SelectPayMode(CartPayMode.All)) },
                            onPaymentClick = { dispatch(CartIntent.SelectPayMode(CartPayMode.Payment)) },
                            onCardsClick = { dispatch(CartIntent.SelectViewMode(CartViewMode.Cards)) },
                            onListClick = { dispatch(CartIntent.SelectViewMode(CartViewMode.List)) }
                        )
                    }
                }
            }
        },
        bottomBar = {
            CartBottomBar(
                onFittingClick = { dispatch(CartIntent.FittingClick) },
                onBuyClick = { dispatch(CartIntent.BuyClick) },
                onChatClick = { dispatch(CartIntent.ChatClick) }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Horizontal)
    ) { innerPadding ->
        when {
            state.products.isEmpty() -> {
                CartListLoading(
                    contentPadding = innerPadding
                )
            }
            else -> {
                ClientLazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding,
                    verticalArrangement = Arrangement.spacedBy(
                        when (state.viewMode) {
                            CartViewMode.List -> 0.dp
                            CartViewMode.Cards -> 24.dp
                        }
                    )
                ) {
                    items(
                        items = state.visibleProducts,
                        key = CartProduct::id
                    ) { product ->
                        when (state.viewMode) {
                            CartViewMode.List -> {
                                CartListProductCard(
                                    product = product,
                                    onClick = { dispatch(CartIntent.ProductClick(product.detailId)) },
                                    onSelectSizeClick = { dispatch(CartIntent.ShowSizePicker(product)) },
                                    onBuySwitchChange = { paySwitch ->
                                        dispatch(CartIntent.ChangePaySwitch(product, paySwitch))
                                    }
                                )
                            }
                            CartViewMode.Cards -> {
                                CartLargeProductCard(
                                    product = product,
                                    onClick = { dispatch(CartIntent.ProductClick(product.detailId)) },
                                    onSelectSizeClick = { dispatch(CartIntent.ShowSizePicker(product)) },
                                    onBuySwitchChange = { paySwitch ->
                                        dispatch(CartIntent.ChangePaySwitch(product, paySwitch))
                                    }
                                )
                            }
                        }
                    }
                    item {
                        CartSummary(
                            state = state
                        )
                    }
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun CartScreenPreview() {
    CartScreenContent(
        state = CartModel(
            products = CartProductProvider().values.toList()
        ),
        dispatch = {}
    )
}
