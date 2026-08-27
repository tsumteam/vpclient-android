@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.cart

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.cart.event.CartEvent
import ru.mercury.vpclient.features.cart.intent.CartIntent
import ru.mercury.vpclient.features.cart.model.CartModel
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.features.cart_edit_product_sheet.CartEditProductSheet
import ru.mercury.vpclient.features.cart_empty_order_dialog.CartEmptyOrderDialog
import ru.mercury.vpclient.features.cart_fitting.CartFittingScreen
import ru.mercury.vpclient.features.cart_fitting_edit_product_sheet.CartFittingEditProductSheet
import ru.mercury.vpclient.features.cart_fitting_empty_order_dialog.CartFittingEmptyOrderDialog
import ru.mercury.vpclient.features.cart_fitting_sheet.CartFittingSheet
import ru.mercury.vpclient.features.cart_list.CartListScreen
import ru.mercury.vpclient.features.color_picker_sheet.ColorPickerSheet
import ru.mercury.vpclient.features.fitting_products_sheet.FittingProductsSheet
import ru.mercury.vpclient.features.fitting_products_sheet.event.FittingProductsEvent
import ru.mercury.vpclient.features.fitting_products_sheet.event.FittingProductsEventManager
import ru.mercury.vpclient.features.messenger_sheet.MessengerSheet
import ru.mercury.vpclient.features.messenger_sheet.event.MessengerEvent
import ru.mercury.vpclient.features.messenger_sheet.event.MessengerEventManager
import ru.mercury.vpclient.features.quantity_picker_sheet.QuantityPickerSheet
import ru.mercury.vpclient.features.size_picker_sheet.SizePickerSheet
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.cart.CartFittingSwitch
import ru.mercury.vpclient.shared.ui.components.messenger.MessengerDock
import ru.mercury.vpclient.shared.ui.components.messenger.MessengerDockState
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.ktx.ObserveAsEvents
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium18

@Composable
fun CartScreen(
    route: CartRoute,
    viewModel: CartViewModel = hiltViewModel<CartViewModel, CartViewModel.Factory>(
        creationCallback = { it.create(route) }
    )
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostStateError = remember { SnackbarHostState() }

    CartScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        snackbarHostStateError = snackbarHostStateError
    )

    if (state.isCartEditProductSheetVisible) {
        CartEditProductSheet(
            state = state.editProductState,
            dispatch = { intent -> viewModel.dispatch(CartIntent.OnCartEditProductIntent(intent)) }
        )
    }

    if (state.isCartFittingEditProductSheetVisible) {
        CartFittingEditProductSheet(
            state = state.fittingEditProductState,
            dispatch = { intent -> viewModel.dispatch(CartIntent.OnCartFittingEditProductIntent(intent)) }
        )
    }

    if (state.isCartFittingSheetVisible) {
        CartFittingSheet(
            state = state.cartFittingState,
            dispatch = { intent -> viewModel.dispatch(CartIntent.OnCartFittingIntent(intent)) }
        )
    }

    if (state.isFittingProductsSheetVisible) {
        FittingProductsSheet()
    }

    if (state.isSizePickerSheetVisible) {
        SizePickerSheet(
            state = state.sizePickerState,
            dispatch = { intent -> viewModel.dispatch(CartIntent.OnSizePickerIntent(intent)) }
        )
    }

    if (state.isColorPickerSheetVisible) {
        ColorPickerSheet(
            state = state.colorPickerState,
            dispatch = { intent -> viewModel.dispatch(CartIntent.OnColorPickerIntent(intent)) }
        )
    }

    if (state.isQuantityPickerSheetVisible) {
        QuantityPickerSheet(
            state = state.quantityPickerState,
            dispatch = { intent -> viewModel.dispatch(CartIntent.OnQuantityPickerIntent(intent)) }
        )
    }

    if (state.isCartEmptyOrderDialogVisible) {
        CartEmptyOrderDialog(
            dispatch = { intent -> viewModel.dispatch(CartIntent.OnCartEmptyOrderIntent(intent)) }
        )
    }

    if (state.isCartFittingEmptyOrderDialogVisible) {
        CartFittingEmptyOrderDialog(
            dispatch = { intent -> viewModel.dispatch(CartIntent.OnCartFittingEmptyOrderIntent(intent)) }
        )
    }

    if (state.isMessengerSheetVisible) {
        MessengerSheet()
    }

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

    ObserveAsEvents(
        flow = FittingProductsEventManager.eventFlow
    ) { event ->
        when (event) {
            is FittingProductsEvent.ConfirmClick -> {
                viewModel.dispatch(CartIntent.ConfirmFittingProductsSheet(event.productIds))
            }
            is FittingProductsEvent.DismissRequest -> {
                viewModel.dispatch(CartIntent.DismissFittingProductsSheet)
            }
        }
    }

    ObserveAsEvents(
        flow = MessengerEventManager.eventFlow
    ) { event ->
        when (event) {
            is MessengerEvent.DismissRequest -> {
                viewModel.dispatch(CartIntent.DismissMessengerSheet)
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
    val pagerState = rememberPagerState(
        initialPage = state.initialPage,
        pageCount = { CartModel.CART_PAGE_COUNT }
    )
    val scope = rememberCoroutineScope()
    val animateToPage = { page: Int ->
        scope.launch {
            pagerState.animateScrollToPage(
                page = page,
                animationSpec = tween(durationMillis = CartModel.CART_PAGE_ANIMATION_DURATION)
            )
        }
    }

    SharedScaffold(
        topBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            ) {
                CenterAlignedTopAppBar(
                    title = {
                        when {
                            state.hasFittingProducts -> {
                                CartFittingSwitch(
                                    selectedIndex = pagerState.currentPage,
                                    onCartClick = { animateToPage(CartModel.CART_PAGE_INDEX) },
                                    onFittingClick = { animateToPage(CartModel.FITTING_PAGE_INDEX) }
                                )
                            }
                            else -> {
                                Text(
                                    text = stringResource(ClientStrings.CartTitle),
                                    style = MaterialTheme.typography.medium18.copy(
                                        color = MaterialTheme.colorScheme.onBackground,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { dispatch(CartIntent.CloseClick) },
                            modifier = Modifier.size(42.dp)
                        ) {
                            Icon(
                                imageVector = Close24,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                    )
                )

            }
        },
        bottomBar = {
            Column {
                MessengerDock(
                    state = MessengerDockState(
                        name = state.activeEmployee.employeeName,
                        brand = state.cartChatBrand,
                        onClick = { dispatch(CartIntent.ChatClick) }
                    )
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsBottomHeight(WindowInsets.navigationBars)
                        .background(MaterialTheme.colorScheme.background)
                )
            }
        },
        snackbarHost = {
            SharedSnackbarHost(
                hostState = snackbarHostStateError,
                modifier = Modifier.padding(bottom = 60.dp),
                containerColor = MaterialTheme.colorScheme.error
            )
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Horizontal)
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            userScrollEnabled = false
        ) { page ->
            when (page) {
                CartModel.CART_PAGE_INDEX -> {
                    CartListScreen(
                        state = state,
                        dispatch = dispatch
                    )
                }
                CartModel.FITTING_PAGE_INDEX -> {
                    CartFittingScreen(
                        state = state,
                        dispatch = dispatch
                    )
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CartScreenPreview(
    @PreviewParameter(CartScreenCartProductProvider::class) state: CartModel
) {
    CartScreenContent(
        state = state,
        dispatch = {},
        snackbarHostStateError = remember { SnackbarHostState() }
    )
}

private class CartScreenCartProductProvider: PreviewParameterProvider<CartModel> {
    private val products = listOf(
        product(
            id = "1",
            brand = "BRUNELLO CUCINELLI",
            name = "Хлопковая футболка с логотипом",
            article = "MP827743",
            color = "Белый",
            size = "IT 48",
            price = "1 600 000 ₽",
            lookId = "look_1",
            lookName = "Образ",
            lookImageUrl = "",
            isForPayment = true,
            priceValue = 1_600_000.0
        ),
        product(
            id = "2",
            brand = "SAINT LAURENT",
            name = "Кожаная куртка",
            article = "SL908221",
            color = "Черный",
            size = "FR 38",
            price = "300 000 ₽",
            oldPrice = "400 000 ₽",
            lookId = "look_1",
            lookName = "Образ",
            lookImageUrl = "",
            isForPayment = false,
            quantity = 2,
            priceValue = 300_000.0
        ),
        product(
            id = "3",
            brand = "LORO PIANA",
            name = "Кашемировый джемпер",
            article = "LP112490",
            color = "Серый",
            size = "M",
            price = "580 000 ₽",
            isForPayment = false,
            isSold = true,
            isAlternativesPaletteOpen = true,
            alternatives = listOf(
                CartProductAlternative(
                    id = "1",
                    detailId = "1",
                    brand = "LORO PIANA",
                    urlBrandLogo = null,
                    price = "580 000 ₽",
                    imageUrl = "",
                    isOriginal = true
                ),
                CartProductAlternative(
                    id = "2",
                    detailId = "2",
                    brand = "DOLCE&GABBANA",
                    urlBrandLogo = null,
                    price = "1 900 000 ₽",
                    imageUrl = "",
                    isOriginal = false
                )
            ),
            priceValue = 580_000.0
        ),
        product(
            id = "4",
            brand = "KITON",
            name = "Шерстяной жакет",
            article = "KT554210",
            color = "Темно-синий",
            size = "",
            price = "920 000 ₽",
            isForPayment = false,
            priceValue = 920_000.0
        )
    )

    override val values: Sequence<CartModel> = sequenceOf(
        CartModel(),
        CartModel(
            products = products
        ),
        CartModel(
            products = products,
            payMode = ru.mercury.vpclient.shared.data.entity.CartPayMode.Payment
        ),
        CartModel(
            products = products,
            isCartFittingSheetVisible = true,
            fittingSheetClientName = "Анна Петровна",
            isFittingSheetClientFeminine = true
        )
    )

    private fun product(
        id: String,
        brand: String,
        name: String,
        article: String,
        color: String,
        size: String,
        price: String,
        isForPayment: Boolean,
        priceValue: Double,
        oldPrice: String? = null,
        lookId: String? = null,
        lookName: String? = null,
        lookImageUrl: String? = null,
        quantity: Int = 1,
        isSold: Boolean = false,
        isAlternativesPaletteOpen: Boolean = false,
        alternatives: List<CartProductAlternative> = emptyList()
    ): CartProduct {
        return CartProduct(
            id = id,
            detailId = id,
            itemId = id,
            colorId = id,
            brand = brand,
            urlBrandLogo = null,
            name = name,
            article = article,
            color = color,
            size = size,
            price = price,
            oldPrice = oldPrice,
            lookId = lookId,
            lookName = lookName,
            lookImageUrl = lookImageUrl,
            imageUrl = "",
            isForPayment = isForPayment,
            quantity = quantity,
            isSold = isSold,
            isAlternativesPaletteOpen = isAlternativesPaletteOpen,
            alternatives = alternatives,
            priceValue = priceValue
        )
    }
}
