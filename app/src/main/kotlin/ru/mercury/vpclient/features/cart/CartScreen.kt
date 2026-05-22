@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.cart.event.CartEvent
import ru.mercury.vpclient.features.cart.intent.CartIntent
import ru.mercury.vpclient.features.cart.model.CartEditProductAction
import ru.mercury.vpclient.features.cart.model.CartModel
import ru.mercury.vpclient.features.cart.model.CartPayMode
import ru.mercury.vpclient.features.cart.model.CartProductGroup
import ru.mercury.vpclient.features.cart.model.CartViewMode
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.SharedTabRow
import ru.mercury.vpclient.shared.ui.components.SharedTabRowState
import ru.mercury.vpclient.shared.ui.components.cart.CartBottomBar
import ru.mercury.vpclient.shared.ui.components.cart.CartEditProductSheet
import ru.mercury.vpclient.shared.ui.components.cart.CartLookCard
import ru.mercury.vpclient.shared.ui.components.cart.CartProductCard
import ru.mercury.vpclient.shared.ui.components.cart.CartProductDivider
import ru.mercury.vpclient.shared.ui.components.cart.CartProductLargeCard
import ru.mercury.vpclient.shared.ui.components.cart.CartSelectSizeDialog
import ru.mercury.vpclient.shared.ui.components.cart.CartSizePickerSheet
import ru.mercury.vpclient.shared.ui.components.cart.CartSummary
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.preview.CartProductProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium13
import ru.mercury.vpclient.shared.ui.theme.medium18
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun CartScreen(
    viewModel: CartViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostStateError = remember { SnackbarHostState() }

    CartScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        snackbarHostStateError = snackbarHostStateError
    )

    ObserveAsEvents(
        flow = viewModel.eventFlow
    ) { event ->
        when (event) {
            is CartEvent.SnackbarErrorMessage -> {
                snackbarHostStateError.currentSnackbarData?.dismiss()
                scope.launch { snackbarHostStateError.showSnackbar(event.message) }
            }
        }
    }
}

@Composable
private fun CartScreenContent(
    state: CartModel,
    dispatch: (CartIntent) -> Unit,
    snackbarHostStateError: SnackbarHostState
) {
    state.editProduct?.let { product ->
        CartEditProductSheet(
            actions = listOf(
                CartEditProductAction(
                    text = stringResource(ClientStrings.CartEditAddSize),
                    onClick = { dispatch(CartIntent.AddSizeClick(product)) }
                ),
                CartEditProductAction(
                    text = stringResource(ClientStrings.CartEditSelectSize),
                    onClick = { dispatch(CartIntent.ShowSizePicker(product)) }
                ),
                CartEditProductAction(
                    text = stringResource(ClientStrings.CartEditChangeQuantity),
                    onClick = { dispatch(CartIntent.ChangeQuantityClick(product)) }
                ),
                CartEditProductAction(
                    text = stringResource(ClientStrings.CartEditChangeColor),
                    onClick = { dispatch(CartIntent.ChangeColorClick(product)) }
                )
            ),
            onDismissRequest = { dispatch(CartIntent.HideEditProductSheet) }
        )
    }

    state.selectSizeProduct?.let { product ->
        CartSelectSizeDialog(
            onSelectSizeClick = { dispatch(CartIntent.SelectSizeClick(product)) },
            onDismissRequest = { dispatch(CartIntent.HideSelectSizeDialog) }
        )
    }

    if (state.sizePickerProduct != null) {
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
                    state.products.isEmpty() -> {
                        Text(
                            text = stringResource(ClientStrings.CartEmptyTabHint),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.regular14.copy(
                                color = MaterialTheme.colorScheme.outline,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                    else -> {
                        SharedTabRow(
                            state = SharedTabRowState(
                                selectedIndex = when (state.payMode) {
                                    CartPayMode.All -> 0
                                    CartPayMode.Payment -> 1
                                },
                                firstTabText = pluralStringResource(
                                    ClientStrings.CartAllItems,
                                    state.allItemsCount,
                                    state.allItemsCount
                                ),
                                secondTabText = pluralStringResource(
                                    ClientStrings.CartPaymentItems,
                                    state.paymentItemsCount,
                                    state.paymentItemsCount
                                ),
                                onFirstTabClick = { dispatch(CartIntent.SelectPayMode(CartPayMode.All)) },
                                onSecondTabClick = { dispatch(CartIntent.SelectPayMode(CartPayMode.Payment)) },
                                isLoading = false
                            ),
                            textStyle = MaterialTheme.typography.medium13,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        },
        bottomBar = {
            CartBottomBar(
                chatName = state.cartChatName,
                chatBrand = state.cartChatBrand,
                onFittingClick = { dispatch(CartIntent.FittingClick) },
                onBuyClick = { dispatch(CartIntent.BuyClick) },
                onChatClick = { dispatch(CartIntent.ChatClick) },
                isActionsEnabled = state.products.isNotEmpty()
            )
        },
        snackbarHost = {
            SharedSnackbarHost(
                hostState = snackbarHostStateError,
                modifier = Modifier.padding(bottom = 8.dp),
                containerColor = MaterialTheme.colorScheme.error
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Horizontal)
    ) { innerPadding ->
        val pullToRefreshState = rememberPullToRefreshState()

        when {
            state.products.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(ClientStrings.CartEmptyMessage),
                        style = MaterialTheme.typography.regular14.copy(
                            color = MaterialTheme.colorScheme.outline,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
            else -> {
                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = { dispatch(CartIntent.PullToRefresh) },
                    state = pullToRefreshState,
                    modifier = Modifier.fillMaxSize(),
                    indicator = {
                        PullToRefreshDefaults.Indicator(
                            state = pullToRefreshState,
                            isRefreshing = state.isRefreshing,
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .padding(top = innerPadding.calculateTopPadding())
                        )
                    }
                ) {
                    SharedLazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = innerPadding,
                        verticalArrangement = Arrangement.spacedBy(
                            when (state.viewMode) {
                                CartViewMode.List -> 0.dp
                                CartViewMode.Cards -> 24.dp
                            }
                        )
                    ) {
                        itemsIndexed(
                            items = state.visibleProductGroups,
                            key = { _, group -> group.key }
                        ) { index, group ->
                            CartProductGroupItem(
                                group = group,
                                viewMode = state.viewMode,
                                selectedAlternativeId = state.selectedAlternativeId,
                                dispatch = dispatch
                            )

                            val product = group.products.firstOrNull()
                            val isAlternativesVisible = product?.isAlternativesPaletteOpen == true &&
                                product.alternatives.isNotEmpty()
                            val isDividerVisible = state.viewMode == CartViewMode.List &&
                                !group.isLook &&
                                index < state.visibleProductGroups.lastIndex &&
                                !isAlternativesVisible

                            if (isDividerVisible) {
                                CartProductDivider()
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
}

@Composable
private fun CartProductGroupItem(
    group: CartProductGroup,
    viewMode: CartViewMode,
    selectedAlternativeId: String?,
    dispatch: (CartIntent) -> Unit
) {
    when {
        group.isLook -> {
            CartLookCard(
                lookName = group.lookName,
                lookImageUrl = group.lookImageUrl,
                products = group.products,
                isLargeCard = viewMode == CartViewMode.Cards,
                onAddClick = {},
                onProductClick = { product -> dispatch(CartIntent.ProductClick(product.detailId)) },
                onSelectSizeClick = { product -> dispatch(CartIntent.ShowSizePicker(product)) },
                onBuySwitchChange = { product, paySwitch ->
                    dispatch(CartIntent.ChangePaySwitch(product, paySwitch))
                },
                onAlternativeClick = { alternative -> dispatch(CartIntent.AlternativeClick(alternative)) },
                onRemoveAlternativeClick = { alternative -> dispatch(CartIntent.RemoveAlternativeClick(alternative)) },
                onHideAlternativesClick = { product -> dispatch(CartIntent.HideAlternativesClick(product)) },
                onEditProductSwipeClick = { product -> dispatch(CartIntent.EditProductSwipeClick(product)) },
                onDeleteProductSwipeClick = { product -> dispatch(CartIntent.DeleteProductSwipeClick(product)) },
                onDetachProductFromLookSwipeClick = { product ->
                    dispatch(CartIntent.DetachProductFromLookSwipeClick(product))
                },
                onReturnOriginalSwipeClick = { product -> dispatch(CartIntent.ReturnOriginalSwipeClick(product)) },
                onShowAlternativesSwipeClick = { product -> dispatch(CartIntent.ShowAlternativesSwipeClick(product)) },
                onHideAlternativesSwipeClick = { product -> dispatch(CartIntent.HideAlternativesSwipeClick(product)) },
                onDisassembleLookSwipeClick = {
                    dispatch(CartIntent.DisassembleLookSwipeClick(group.lookId.orEmpty()))
                },
                onDeleteLookSwipeClick = {
                    dispatch(CartIntent.DeleteLookSwipeClick(group.lookId.orEmpty()))
                },
                selectedAlternativeId = selectedAlternativeId
            )
        }
        else -> {
            val product = group.products.first()
            when (viewMode) {
                CartViewMode.List -> {
                    CartProductCard(
                        product = product,
                        onClick = { dispatch(CartIntent.ProductClick(product.detailId)) },
                        onSelectSizeClick = { dispatch(CartIntent.ShowSizePicker(product)) },
                        onBuySwitchChange = { paySwitch ->
                            dispatch(CartIntent.ChangePaySwitch(product, paySwitch))
                        },
                        onAlternativeClick = { alternative ->
                            dispatch(CartIntent.AlternativeClick(alternative))
                        },
                        onRemoveAlternativeClick = { alternative ->
                            dispatch(CartIntent.RemoveAlternativeClick(alternative))
                        },
                        onHideAlternativesClick = {
                            dispatch(CartIntent.HideAlternativesClick(product))
                        },
                        onEditSwipeClick = {
                            dispatch(CartIntent.EditProductSwipeClick(product))
                        },
                        onDeleteSwipeClick = {
                            dispatch(CartIntent.DeleteProductSwipeClick(product))
                        },
                        onDetachFromLookSwipeClick = {
                            dispatch(CartIntent.DetachProductFromLookSwipeClick(product))
                        },
                        onReturnOriginalSwipeClick = {
                            dispatch(CartIntent.ReturnOriginalSwipeClick(product))
                        },
                        onShowAlternativesSwipeClick = {
                            dispatch(CartIntent.ShowAlternativesSwipeClick(product))
                        },
                        onHideAlternativesSwipeClick = {
                            dispatch(CartIntent.HideAlternativesSwipeClick(product))
                        },
                        selectedAlternativeId = selectedAlternativeId
                    )
                }
                CartViewMode.Cards -> {
                    CartProductLargeCard(
                        product = product,
                        onClick = { dispatch(CartIntent.ProductClick(product.detailId)) },
                        onSelectSizeClick = { dispatch(CartIntent.ShowSizePicker(product)) },
                        onBuySwitchChange = { paySwitch ->
                            dispatch(CartIntent.ChangePaySwitch(product, paySwitch))
                        },
                        onAlternativeClick = { alternative ->
                            dispatch(CartIntent.AlternativeClick(alternative))
                        },
                        onRemoveAlternativeClick = { alternative ->
                            dispatch(CartIntent.RemoveAlternativeClick(alternative))
                        },
                        onHideAlternativesClick = {
                            dispatch(CartIntent.HideAlternativesClick(product))
                        },
                        onEditSwipeClick = {
                            dispatch(CartIntent.EditProductSwipeClick(product))
                        },
                        onDeleteSwipeClick = {
                            dispatch(CartIntent.DeleteProductSwipeClick(product))
                        },
                        onDetachFromLookSwipeClick = {
                            dispatch(CartIntent.DetachProductFromLookSwipeClick(product))
                        },
                        onReturnOriginalSwipeClick = {
                            dispatch(CartIntent.ReturnOriginalSwipeClick(product))
                        },
                        onShowAlternativesSwipeClick = {
                            dispatch(CartIntent.ShowAlternativesSwipeClick(product))
                        },
                        onHideAlternativesSwipeClick = {
                            dispatch(CartIntent.HideAlternativesSwipeClick(product))
                        },
                        selectedAlternativeId = selectedAlternativeId
                    )
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
        dispatch = {},
        snackbarHostStateError = remember { SnackbarHostState() }
    )
}
