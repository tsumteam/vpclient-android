package ru.mercury.vpclient.features.cart_fitting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.features.cart.intent.CartIntent
import ru.mercury.vpclient.features.cart.model.CartModel
import ru.mercury.vpclient.shared.data.entity.CartPayMode
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartViewMode
import ru.mercury.vpclient.shared.data.entity.FittingDeliveryData
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedPullToRefreshBox
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedTabRow
import ru.mercury.vpclient.shared.ui.components.SharedTabRowState
import ru.mercury.vpclient.shared.ui.components.cart.CartFittingSummary
import ru.mercury.vpclient.shared.ui.components.cart.CartLookCard
import ru.mercury.vpclient.shared.ui.components.cart.CartProductCard
import ru.mercury.vpclient.shared.ui.components.cart.CartProductCardState
import ru.mercury.vpclient.shared.ui.components.cart.CartProductLargeCard
import ru.mercury.vpclient.shared.ui.components.fitting.FittingDeliveryHeader
import ru.mercury.vpclient.shared.ui.components.fitting.FittingDeliveryHeaderState
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.divider
import ru.mercury.vpclient.shared.ui.theme.medium13
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun CartFittingScreen(
    state: CartModel,
    dispatch: (CartIntent) -> Unit
) {
    CartFittingScreenContent(
        state = state,
        dispatch = dispatch
    )
}

@Composable
private fun CartFittingScreenContent(
    state: CartModel,
    dispatch: (CartIntent) -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()

    SharedScaffold(
        topBar = {
            when {
                state.apiFittingProducts.isEmpty() -> {
                    Text(
                        text = stringResource(ClientStrings.CartEmptyTabHint),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
                        style = MaterialTheme.typography.regular14.copy(
                            color = MaterialTheme.colorScheme.outline,
                            textAlign = TextAlign.Center
                        )
                    )
                }
                else -> {
                    Box(
                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    ) {
                        SharedTabRow(
                            state = SharedTabRowState(
                                selectedIndex = when (state.payMode) {
                                    CartPayMode.All -> 0
                                    CartPayMode.Payment -> 1
                                },
                                firstTabText = pluralStringResource(
                                    ClientStrings.CartAllItems,
                                    state.apiFittingProductsCount,
                                    state.apiFittingProductsCount
                                ),
                                secondTabText = pluralStringResource(
                                    ClientStrings.CartPaymentItems,
                                    state.apiFittingPaymentProductsCount,
                                    state.apiFittingPaymentProductsCount
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
        floatingActionButton = {
            Button(
                onClick = { dispatch(CartIntent.BuyClick) },
                enabled = state.apiFittingProducts.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(ClientStrings.CartBuy),
                    style = MaterialTheme.typography.medium15.copy(
                        letterSpacing = .3.sp
                    )
                )
            }
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Horizontal)
    ) { innerPadding ->
        when {
            state.apiFittingProducts.isEmpty() -> {
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
                SharedPullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = { dispatch(CartIntent.PullToRefresh) },
                    modifier = Modifier.fillMaxSize(),
                    state = pullToRefreshState,
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
                        contentPadding = innerPadding + PaddingValues(bottom = 76.dp),
                        verticalArrangement = Arrangement.spacedBy(
                            when (state.viewMode) {
                                CartViewMode.List -> 0.dp
                                CartViewMode.Cards -> 24.dp
                            }
                        )
                    ) {
                        state.visibleFittingDeliveryGroups.forEachIndexed { deliveryIndex, deliveryGroup ->
                            item(
                                key = "fitting_delivery_header_${deliveryIndex}_${deliveryGroup.header.date}_${deliveryGroup.header.address}"
                            ) {
                                val productIds = deliveryGroup.productGroups
                                    .flatMap { group -> group.products }
                                    .map { product -> product.id }

                                if (deliveryIndex > 0) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        color = MaterialTheme.colorScheme.divider
                                    )
                                }

                                FittingDeliveryHeader(
                                    header = deliveryGroup.header,
                                    onClick = {
                                        dispatch(
                                            CartIntent.FittingDeliveryClick(
                                                productIds = productIds,
                                                deliveryId = deliveryGroup.id,
                                                fittingType = deliveryGroup.fittingType,
                                                header = deliveryGroup.header
                                            )
                                        )
                                    }
                                )

                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = MaterialTheme.colorScheme.divider
                                )
                            }

                            itemsIndexed(
                                items = deliveryGroup.productGroups,
                                key = { _, group -> "${deliveryIndex}_${group.key}" }
                            ) { index, group ->
                                when {
                                    group.isLook -> {
                                        CartLookCard(
                                            lookName = group.lookName,
                                            lookImageUrl = group.lookImageUrl,
                                            products = group.products,
                                            isLargeCard = state.viewMode == CartViewMode.Cards,
                                            onAddClick = {},
                                            onProductClick = { product -> dispatch(CartIntent.ProductClick(product.detailId)) },
                                            onSelectSizeClick = { product -> dispatch(CartIntent.ShowSizePicker(product)) },
                                            onBuySwitchChange = { product, paySwitch ->
                                                dispatch(CartIntent.ChangeFittingPaySwitch(product, paySwitch))
                                            },
                                            onAlternativeClick = { alternative -> dispatch(CartIntent.AlternativeClick(alternative)) },
                                            onRemoveAlternativeClick = { alternative -> dispatch(CartIntent.RemoveAlternativeClick(alternative)) },
                                            onHideAlternativesClick = { product -> dispatch(CartIntent.HideAlternativesClick(product)) },
                                            onEditProductSwipeClick = { product ->
                                                dispatch(CartIntent.EditFittingProductSwipeClick(product))
                                            },
                                            onDeleteProductSwipeClick = { product -> dispatch(CartIntent.DeleteProductSwipeClick(product)) },
                                            onDetachProductFromLookSwipeClick = { product ->
                                                dispatch(CartIntent.DetachProductFromLookSwipeClick(product))
                                            },
                                            onReturnOriginalSwipeClick = { product ->
                                                dispatch(CartIntent.ReturnOriginalSwipeClick(product))
                                            },
                                            onShowAlternativesSwipeClick = { product ->
                                                dispatch(CartIntent.ShowAlternativesSwipeClick(product))
                                            },
                                            onHideAlternativesSwipeClick = { product ->
                                                dispatch(CartIntent.HideAlternativesSwipeClick(product))
                                            },
                                            onReturnToBasketSwipeClick = { product ->
                                                dispatch(CartIntent.ReturnFittingProductToBasketSwipeClick(product))
                                            },
                                            useFittingProductSwipeActions = true,
                                            onDisassembleLookSwipeClick = {
                                                dispatch(CartIntent.DisassembleLookSwipeClick(group.lookId.orEmpty()))
                                            },
                                            onDeleteLookSwipeClick = {
                                                dispatch(CartIntent.DeleteLookSwipeClick(group.lookId.orEmpty()))
                                            },
                                            productModifier = { Modifier },
                                            selectedAlternativeId = state.selectedAlternativeId
                                        )
                                    }
                                    else -> {
                                        val product = group.products.first()

                                        when (state.viewMode) {
                                            CartViewMode.List -> {
                                                CartProductCard(
                                                    state = CartProductCardState(
                                                        product = product,
                                                        onClick = { dispatch(CartIntent.ProductClick(product.detailId)) },
                                                        onSelectSizeClick = { dispatch(CartIntent.ShowSizePicker(product)) },
                                                        onBuySwitchChange = { paySwitch ->
                                                            dispatch(CartIntent.ChangeFittingPaySwitch(product, paySwitch))
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
                                                            dispatch(CartIntent.EditFittingProductSwipeClick(product))
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
                                                        onReturnToBasketSwipeClick = {
                                                            dispatch(CartIntent.ReturnFittingProductToBasketSwipeClick(product))
                                                        },
                                                        useFittingSwipeActions = true,
                                                        selectedAlternativeId = state.selectedAlternativeId
                                                    )
                                                )
                                            }
                                            CartViewMode.Cards -> {
                                                CartProductLargeCard(
                                                    product = product,
                                                    onClick = { dispatch(CartIntent.ProductClick(product.detailId)) },
                                                    onSelectSizeClick = { dispatch(CartIntent.ShowSizePicker(product)) },
                                                    onBuySwitchChange = { paySwitch ->
                                                        dispatch(CartIntent.ChangeFittingPaySwitch(product, paySwitch))
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
                                                        dispatch(CartIntent.EditFittingProductSwipeClick(product))
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
                                                    onReturnToBasketSwipeClick = {
                                                        dispatch(CartIntent.ReturnFittingProductToBasketSwipeClick(product))
                                                    },
                                                    useFittingSwipeActions = true,
                                                    selectedAlternativeId = state.selectedAlternativeId
                                                )
                                            }
                                        }
                                    }
                                }

                                val product = group.products.firstOrNull()
                                val isAlternativesVisible = product?.isAlternativesPaletteOpen == true &&
                                    product.alternatives.isNotEmpty()
                                val isDividerVisible = state.viewMode == CartViewMode.List &&
                                    !group.isLook &&
                                    index < deliveryGroup.productGroups.lastIndex &&
                                    !isAlternativesVisible

                                if (isDividerVisible) {
                                    HorizontalDivider(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        color = MaterialTheme.colorScheme.divider
                                    )
                                }
                            }
                        }

                        item {
                            val lastGroup = state.visibleFittingDeliveryGroups
                                .lastOrNull()
                                ?.productGroups
                                ?.lastOrNull()

                            if (lastGroup?.isLook != true) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = MaterialTheme.colorScheme.divider
                                )
                            }

                            CartFittingSummary(
                                fittingSummary = state.apiFittingProductsSummary,
                                paymentSummary = state.apiFittingPaymentProductsSummary
                            )
                        }
                    }
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CartFittingScreenPreview(
    @PreviewParameter(CartFittingScreenModelProvider::class) state: CartModel
) {
    CartFittingScreenContent(
        state = state,
        dispatch = {}
    )
}

private class CartFittingScreenModelProvider: PreviewParameterProvider<CartModel> {
    private val products = listOf(
        CartProduct(
            id = "1",
            detailId = "1",
            itemId = "1",
            colorId = "1",
            brand = "BRUNELLO CUCINELLI",
            urlBrandLogo = null,
            name = "Хлопковая футболка",
            article = "MP827743",
            color = "Белый",
            size = "IT 48",
            price = "1 600 000 ₽",
            imageUrl = "",
            isForPayment = true,
            priceValue = 1_600_000.0
        ),
        CartProduct(
            id = "2",
            detailId = "2",
            itemId = "2",
            colorId = "2",
            brand = "SAINT LAURENT",
            urlBrandLogo = null,
            name = "Кожаная куртка",
            article = "SL908221",
            color = "Черный",
            size = "FR 38",
            price = "300 000 ₽",
            imageUrl = "",
            isForPayment = false,
            priceValue = 300_000.0
        )
    )

    override val values: Sequence<CartModel> = sequenceOf(
        CartModel(),
        CartModel(
            apiFittingProducts = products,
            apiFittingDeliveries = listOf(
                FittingDeliveryData(
                    id = "delivery_1",
                    header = FittingDeliveryHeaderState(
                        status = "Запланирована",
                        date = "Сегодня, 18:00-20:00",
                        address = "Москва, Тверская, 14",
                        isDelivered = false
                    ),
                    products = products
                )
            )
        ),
        CartModel(
            viewMode = CartViewMode.Cards,
            payMode = CartPayMode.Payment,
            apiFittingProducts = products,
            apiFittingDeliveries = listOf(
                FittingDeliveryData(
                    id = "delivery_1",
                    header = FittingDeliveryHeaderState(
                        status = "Доставлена",
                        date = "Вчера, 12:00-14:00",
                        address = "Москва, Петровка, 7",
                        isDelivered = true
                    ),
                    products = products
                )
            )
        )
    )
}
