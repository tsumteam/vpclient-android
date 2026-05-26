@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.main.tabs.fitting

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import ru.mercury.vpclient.features.cart.model.CartPayMode
import ru.mercury.vpclient.features.main.tabs.fitting.intent.FittingIntent
import ru.mercury.vpclient.features.main.tabs.fitting.model.FittingModel
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.FittingDeliveryHeader
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedTabRow
import ru.mercury.vpclient.shared.ui.components.SharedTabRowState
import ru.mercury.vpclient.shared.ui.components.cart.CartActionButton
import ru.mercury.vpclient.shared.ui.components.cart.CartActionsBar
import ru.mercury.vpclient.shared.ui.components.cart.CartFittingDeliveryHeader
import ru.mercury.vpclient.shared.ui.components.cart.CartFittingSummary
import ru.mercury.vpclient.shared.ui.components.cart.CartFittingSwitch
import ru.mercury.vpclient.shared.ui.components.cart.CartProductCard
import ru.mercury.vpclient.shared.ui.components.cart.CartProductDivider
import ru.mercury.vpclient.shared.ui.components.cart.MessengerIconButton
import ru.mercury.vpclient.shared.ui.icons.Search24
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium13
import ru.mercury.vpclient.shared.ui.theme.regular14

private const val FITTING_CART_PAGE_INDEX = 0
private const val FITTING_PAGE_INDEX = 1
private const val FITTING_PAGE_COUNT = 2

@Composable
fun FittingScreen(
    viewModel: FittingViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()

    FittingScreenContent(
        state = state,
        dispatch = viewModel::dispatch
    )
}

@Composable
private fun FittingScreenContent(
    state: FittingModel,
    dispatch: (FittingIntent) -> Unit
) {
    val pagerState = rememberPagerState(
        initialPage = FITTING_PAGE_INDEX,
        pageCount = { FITTING_PAGE_COUNT }
    )
    val scope = rememberCoroutineScope()
    val animateToPage = { page: Int ->
        scope.launch {
            pagerState.animateScrollToPage(
                page = page,
                animationSpec = tween(durationMillis = 350)
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    CartFittingSwitch(
                        selectedIndex = pagerState.currentPage,
                        onCartClick = { animateToPage(FITTING_CART_PAGE_INDEX) },
                        onFittingClick = { animateToPage(FITTING_PAGE_INDEX) }
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { dispatch(FittingIntent.SearchClick) }
                    ) {
                        Icon(
                            imageVector = Search24,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    MessengerIconButton(
                        showBadge = state.showMessengerBadge,
                        onClick = { dispatch(FittingIntent.MessengerClick) }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            userScrollEnabled = false
        ) { page ->
            when (page) {
                FITTING_CART_PAGE_INDEX -> {
                    FittingProductsPage(
                        products = state.products,
                        visibleProducts = state.visibleProducts,
                        selectedPayMode = state.payMode,
                        allItemsCount = state.allItemsCount,
                        paymentItemsCount = state.paymentItemsCount,
                        bottomBar = {
                            CartActionsBar(
                                onFittingClick = { animateToPage(FITTING_PAGE_INDEX) },
                                onBuyClick = { dispatch(FittingIntent.BuyClick) },
                                isActionsEnabled = state.products.isNotEmpty()
                            )
                        },
                        dispatch = dispatch
                    )
                }
                FITTING_PAGE_INDEX -> {
                    FittingProductsPage(
                        products = state.fittingProducts,
                        visibleProducts = state.visibleFittingProducts,
                        selectedPayMode = state.payMode,
                        allItemsCount = state.fittingProductsCount,
                        paymentItemsCount = state.fittingPaymentProductsCount,
                        fittingSummary = state.fittingSummary,
                        fittingPaymentSummary = state.fittingPaymentSummary,
                        fittingDeliveryHeader = state.apiFittingDeliveryHeader,
                        showFittingDeliveryHeader = state.payMode == CartPayMode.All,
                        bottomBar = {
                            FittingBuyActionBar(
                                onClick = { dispatch(FittingIntent.BuyClick) },
                                enabled = state.fittingProducts.isNotEmpty()
                            )
                        },
                        dispatch = dispatch
                    )
                }
            }
        }
    }
}

@Composable
private fun FittingProductsPage(
    products: List<CartProduct>,
    visibleProducts: List<CartProduct>,
    selectedPayMode: CartPayMode,
    allItemsCount: Int,
    paymentItemsCount: Int,
    fittingSummary: String = "",
    fittingPaymentSummary: String = "",
    fittingDeliveryHeader: FittingDeliveryHeader = FittingDeliveryHeader.Empty,
    showFittingDeliveryHeader: Boolean = false,
    bottomBar: @Composable () -> Unit,
    dispatch: (FittingIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            FittingPageTabs(
                products = products,
                selectedPayMode = selectedPayMode,
                allItemsCount = allItemsCount,
                paymentItemsCount = paymentItemsCount,
                dispatch = dispatch
            )
        },
        bottomBar = bottomBar,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Horizontal)
    ) { innerPadding ->
        FittingProductsPageContent(
            products = products,
            visibleProducts = visibleProducts,
            fittingSummary = fittingSummary,
            fittingPaymentSummary = fittingPaymentSummary,
            fittingDeliveryHeader = fittingDeliveryHeader,
            showFittingDeliveryHeader = showFittingDeliveryHeader,
            contentPadding = innerPadding,
            dispatch = dispatch
        )
    }
}

@Composable
private fun FittingPageTabs(
    products: List<CartProduct>,
    selectedPayMode: CartPayMode,
    allItemsCount: Int,
    paymentItemsCount: Int,
    dispatch: (FittingIntent) -> Unit
) {
    when {
        products.isEmpty() -> {
            Text(
                text = stringResource(ClientStrings.CartEmptyTabHint),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Center
                )
            )
        }
        else -> {
            SharedTabRow(
                state = SharedTabRowState(
                    selectedIndex = when (selectedPayMode) {
                        CartPayMode.All -> 0
                        CartPayMode.Payment -> 1
                    },
                    firstTabText = pluralStringResource(
                        ClientStrings.CartAllItems,
                        allItemsCount,
                        allItemsCount
                    ),
                    secondTabText = pluralStringResource(
                        ClientStrings.CartPaymentItems,
                        paymentItemsCount,
                        paymentItemsCount
                    ),
                    onFirstTabClick = { dispatch(FittingIntent.SelectPayMode(CartPayMode.All)) },
                    onSecondTabClick = { dispatch(FittingIntent.SelectPayMode(CartPayMode.Payment)) },
                    isLoading = false
                ),
                textStyle = MaterialTheme.typography.medium13,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
private fun FittingProductsPageContent(
    products: List<CartProduct>,
    visibleProducts: List<CartProduct>,
    fittingSummary: String,
    fittingPaymentSummary: String,
    fittingDeliveryHeader: FittingDeliveryHeader,
    showFittingDeliveryHeader: Boolean,
    contentPadding: PaddingValues,
    dispatch: (FittingIntent) -> Unit
) {
    when {
        products.isEmpty() -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(start = 32.dp, end = 32.dp),
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
            SharedLazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding
            ) {
                if (showFittingDeliveryHeader) {
                    item {
                        CartFittingDeliveryHeader(
                            header = fittingDeliveryHeader,
                            onClick = { dispatch(FittingIntent.FittingDeliveryClick) }
                        )

                        CartProductDivider()
                    }
                }

                items(
                    items = visibleProducts,
                    key = { product -> product.id }
                ) { product ->
                    CartProductCard(
                        product = product,
                        onClick = { dispatch(FittingIntent.ProductClick(product.detailId)) },
                        onBuySwitchChange = { paySwitch ->
                            dispatch(FittingIntent.ChangePaySwitch(product, paySwitch))
                        }
                    )

                    if (product != visibleProducts.lastOrNull() || showFittingDeliveryHeader) {
                        CartProductDivider()
                    }
                }

                if (fittingSummary.isNotEmpty()) {
                    item {
                        CartFittingSummary(
                            fittingSummary = fittingSummary,
                            paymentSummary = fittingPaymentSummary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FittingBuyActionBar(
    onClick: () -> Unit,
    enabled: Boolean
) {
    CartActionButton(
        text = stringResource(ClientStrings.CartBuy),
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 16.dp)
    )
}

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun FittingScreenContentPreview() {
    FittingScreenContent(
        state = FittingModel(),
        dispatch = {}
    )
}
