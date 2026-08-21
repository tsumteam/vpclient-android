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
import ru.mercury.vpclient.features.cart_edit_product_sheet.intent.CartEditProductIntent
import ru.mercury.vpclient.features.cart_empty_order_dialog.CartEmptyOrderDialog
import ru.mercury.vpclient.features.cart_empty_order_dialog.intent.CartEmptyOrderIntent
import ru.mercury.vpclient.features.cart_fitting.CartFittingScreen
import ru.mercury.vpclient.features.cart_fitting_edit_product_sheet.CartFittingEditProductSheet
import ru.mercury.vpclient.features.cart_fitting_edit_product_sheet.intent.CartFittingEditProductIntent
import ru.mercury.vpclient.features.cart_fitting_empty_order_dialog.CartFittingEmptyOrderDialog
import ru.mercury.vpclient.features.cart_fitting_empty_order_dialog.intent.CartFittingEmptyOrderIntent
import ru.mercury.vpclient.features.cart_fitting_sheet.CartFittingSheet
import ru.mercury.vpclient.features.cart_fitting_sheet.intent.CartFittingIntent
import ru.mercury.vpclient.features.cart_list.CartListScreen
import ru.mercury.vpclient.features.cart_size_sheet.CartSizeSheet
import ru.mercury.vpclient.features.cart_size_sheet.intent.CartSizeSheetIntent
import ru.mercury.vpclient.features.cart_size_sheet.model.CartSizeSheetModel
import ru.mercury.vpclient.features.color_picker_sheet.ColorPickerSheet
import ru.mercury.vpclient.features.color_picker_sheet.intent.ColorPickerIntent
import ru.mercury.vpclient.features.fitting_products_sheet.FittingProductsSheet
import ru.mercury.vpclient.features.fitting_products_sheet.event.FittingProductsSheetEvent
import ru.mercury.vpclient.features.fitting_products_sheet.event.FittingProductsSheetEventManager
import ru.mercury.vpclient.features.quantity_picker_sheet.QuantityPickerSheet
import ru.mercury.vpclient.features.quantity_picker_sheet.intent.QuantityPickerIntent
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.cart.CartChatDock
import ru.mercury.vpclient.shared.ui.components.cart.CartChatDockState
import ru.mercury.vpclient.shared.ui.components.cart.CartFittingSwitch
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

    if (state.isEditProductSheetVisible) {
        CartEditProductSheet(
            state = state.editProductModel,
            dispatch = { intent ->
                when (intent) {
                    is CartEditProductIntent.ActionClick -> {
                        viewModel.dispatch(CartIntent.DismissCartEditProductSheet)
                        viewModel.dispatch(state.editProductActions[intent.index].second)
                    }
                    is CartEditProductIntent.DismissClick -> {
                        viewModel.dispatch(CartIntent.DismissCartEditProductSheet)
                    }
                }
            }
        )
    }

    if (state.isCartFittingEditProductSheetVisible) {
        CartFittingEditProductSheet(
            state = state.fittingEditProductModel,
            dispatch = { intent ->
                when (intent) {
                    is CartFittingEditProductIntent.ChangeColorClick -> {
                        viewModel.dispatch(CartIntent.ShowFittingColorPicker)
                    }
                    is CartFittingEditProductIntent.ChangeSizeClick -> {
                        viewModel.dispatch(CartIntent.ShowFittingSizePicker)
                    }
                    is CartFittingEditProductIntent.DismissClick -> {
                        viewModel.dispatch(CartIntent.DismissCartFittingEditProductSheet)
                    }
                }
            }
        )
    }

    if (state.isCartFittingSheetVisible) {
        CartFittingSheet(
            state = state.cartFittingModel,
            dispatch = { intent ->
                when (intent) {
                    is CartFittingIntent.ConfirmClick -> {
                        viewModel.dispatch(CartIntent.ConfirmFittingSheet(intent.option))
                    }
                    is CartFittingIntent.DismissClick -> {
                        viewModel.dispatch(CartIntent.DismissCartFittingSheet)
                    }
                }
            }
        )
    }

    if (state.isFittingProductsSheetVisible) {
        FittingProductsSheet()
    }

    if (state.isSizePickerSheetVisible) {
        CartSizeSheet(
            state = CartSizeSheetModel(
                sizeSelectorState = state.sizePickerState,
                buttonText = when {
                    state.sizePickerForFitting -> stringResource(ClientStrings.CartSave)
                    else -> stringResource(ClientStrings.CartSizeSheetSelect)
                }
            ),
            dispatch = { intent ->
                when (intent) {
                    is CartSizeSheetIntent.SizeClick -> {
                        viewModel.dispatch(CartIntent.ToggleSizePickerItem(intent.index))
                    }
                    is CartSizeSheetIntent.ConfirmClick -> {
                        viewModel.dispatch(CartIntent.ConfirmSizePicker)
                    }
                    is CartSizeSheetIntent.DismissRequest -> {
                        viewModel.dispatch(CartIntent.HideSizePicker)
                    }
                    is CartSizeSheetIntent.SizeTableClick -> {
                        viewModel.dispatch(CartIntent.SizeTableClick)
                    }
                }
            }
        )
    }

    if (state.isColorPickerSheetVisible) {
        ColorPickerSheet(
            state = state.colorPickerModel,
            dispatch = { intent ->
                when (intent) {
                    is ColorPickerIntent.ColorClick -> {
                        viewModel.dispatch(CartIntent.ToggleColorPickerItem(intent.index))
                    }
                    is ColorPickerIntent.ConfirmClick -> {
                        viewModel.dispatch(CartIntent.ConfirmColorPicker)
                    }
                    is ColorPickerIntent.DismissClick -> {
                        viewModel.dispatch(CartIntent.DismissColorPickerSheet)
                    }
                }
            }
        )
    }

    if (state.isQuantityPickerSheetVisible) {
        QuantityPickerSheet(
            state = state.quantityPickerModel,
            dispatch = { intent ->
                when (intent) {
                    is QuantityPickerIntent.QuantityClick -> {
                        viewModel.dispatch(CartIntent.ToggleQuantityPickerItem(intent.index))
                    }
                    is QuantityPickerIntent.ConfirmClick -> {
                        viewModel.dispatch(CartIntent.ConfirmQuantityPicker)
                    }
                    is QuantityPickerIntent.DismissClick -> {
                        viewModel.dispatch(CartIntent.DismissQuantityPickerSheet)
                    }
                }
            }
        )
    }

    if (state.isCartEmptyOrderDialogVisible) {
        CartEmptyOrderDialog(
            dispatch = { intent ->
                when (intent) {
                    is CartEmptyOrderIntent.DismissRequest -> {
                        viewModel.dispatch(CartIntent.DismissCartEmptyOrderDialog)
                    }
                }
            }
        )
    }

    if (state.isCartFittingEmptyOrderDialogVisible) {
        CartFittingEmptyOrderDialog(
            dispatch = { intent ->
                when (intent) {
                    is CartFittingEmptyOrderIntent.DismissRequest -> {
                        viewModel.dispatch(CartIntent.DismissCartFittingEmptyOrderDialog)
                    }
                }
            }
        )
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
        flow = FittingProductsSheetEventManager.eventFlow
    ) { event ->
        when (event) {
            is FittingProductsSheetEvent.ConfirmClick -> {
                viewModel.dispatch(CartIntent.ConfirmFittingProductsSheet(event.productIds))
            }
            is FittingProductsSheetEvent.DismissRequest -> {
                viewModel.dispatch(CartIntent.HideFittingProductsSheet)
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
                CartChatDock(
                    state = CartChatDockState(
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
