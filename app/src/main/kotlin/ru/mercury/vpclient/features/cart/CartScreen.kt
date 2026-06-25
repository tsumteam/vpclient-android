@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.cart

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import ru.mercury.vpclient.features.cart_color_sheet.CartColorSheet
import ru.mercury.vpclient.features.cart_color_sheet.intent.CartColorIntent
import ru.mercury.vpclient.features.cart_color_sheet.model.CartColorModel
import ru.mercury.vpclient.features.cart_edit_product_sheet.CartEditProductSheet
import ru.mercury.vpclient.features.cart_edit_product_sheet.intent.CartEditProductSheetIntent
import ru.mercury.vpclient.features.cart_edit_product_sheet.model.CartEditProductSheetModel
import ru.mercury.vpclient.features.cart_fitting.CartFittingScreen
import ru.mercury.vpclient.features.cart_fitting_edit_product_sheet.CartFittingEditProductSheet
import ru.mercury.vpclient.features.cart_fitting_edit_product_sheet.intent.CartFittingEditProductSheetIntent
import ru.mercury.vpclient.features.cart_fitting_products_sheet.CartFittingProductsSheet
import ru.mercury.vpclient.features.cart_fitting_products_sheet.intent.CartFittingProductsSheetIntent
import ru.mercury.vpclient.features.cart_fitting_products_sheet.model.CartFittingProductsSheetModel
import ru.mercury.vpclient.features.cart_fitting_sheet.CartFittingSheet
import ru.mercury.vpclient.features.cart_fitting_sheet.intent.CartFittingSheetIntent
import ru.mercury.vpclient.features.cart_fitting_sheet.model.CartFittingSheetModel
import ru.mercury.vpclient.features.cart_list.CartListScreen
import ru.mercury.vpclient.features.cart_quantity_sheet.CartQuantitySheet
import ru.mercury.vpclient.features.cart_quantity_sheet.intent.CartQuantityIntent
import ru.mercury.vpclient.features.cart_quantity_sheet.model.CartQuantityModel
import ru.mercury.vpclient.features.cart_size_sheet.CartSizeSheet
import ru.mercury.vpclient.features.cart_size_sheet.intent.CartSizeSheetIntent
import ru.mercury.vpclient.features.cart_size_sheet.model.CartSizeSheetModel
import ru.mercury.vpclient.shared.data.entity.CartFittingSheetOption
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.cart.CartChatDock
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
    val dispatch = viewModel::dispatch
    val scope = rememberCoroutineScope()
    val snackbarHostStateError = remember { SnackbarHostState() }

    CartScreenContent(
        state = state,
        dispatch = dispatch,
        snackbarHostStateError = snackbarHostStateError
    )

    if (state.isEditProductSheetVisible) {
        val product = requireNotNull(state.editProduct)
        val editProductActions = buildList {
            if (!product.isSold && product.isSizeSelectionAvailable && product.sizeItems.size < 2) {
                add(stringResource(ClientStrings.CartEditAddSize) to CartIntent.AddSizeClick(product))
            }
            if (product.isSizeSelectionAvailable) {
                add(stringResource(ClientStrings.CartEditSelectSize) to CartIntent.ShowSizePicker(product))
            }
            if (!product.isSold) {
                add(stringResource(ClientStrings.CartEditChangeQuantity) to CartIntent.ShowQuantityPicker(product))
            }
            add(stringResource(ClientStrings.CartEditChangeColor) to CartIntent.ShowColorPicker(product))
        }

        CartEditProductSheet(
            state = CartEditProductSheetModel(
                actions = editProductActions.map { it.first }
            ),
            dispatch = { intent ->
                when (intent) {
                    is CartEditProductSheetIntent.ActionClick -> {
                        dispatch(CartIntent.HideEditProductSheet)
                        editProductActions.getOrNull(intent.index)?.second?.let(dispatch)
                    }
                    is CartEditProductSheetIntent.DismissRequest -> {
                        dispatch(CartIntent.HideEditProductSheet)
                    }
                }
            }
        )
    }

    if (state.isFittingEditProductSheetVisible) {
        val product = requireNotNull(state.fittingEditProduct)
        CartFittingEditProductSheet(
            dispatch = { intent ->
                when (intent) {
                    is CartFittingEditProductSheetIntent.ChangeColorClick -> {
                        dispatch(CartIntent.ShowColorPicker(product, forFitting = true))
                    }
                    is CartFittingEditProductSheetIntent.ChangeSizeClick -> {
                        dispatch(CartIntent.ShowFittingSizePicker(product))
                    }
                    is CartFittingEditProductSheetIntent.ConfirmClick -> {
                        dispatch(CartIntent.HideFittingEditProductSheet)
                    }
                    is CartFittingEditProductSheetIntent.DismissRequest -> {
                        dispatch(CartIntent.HideFittingEditProductSheet)
                    }
                }
            }
        )
    }

    if (state.isFittingSheetVisible) {
        CartFittingSheet(
            state = CartFittingSheetModel(
                clientName = state.fittingSheetClientName,
                clientFeminine = state.isFittingSheetClientFeminine,
                allProductsCount = state.fittingProductsCount,
                allProductsSummary = state.fittingProductsSummary,
                paymentProductsCount = state.fittingPaymentProductsCount,
                paymentProductsSummary = state.fittingPaymentProductsSummary,
                hasProductsWithoutSize = state.hasProductsWithoutSize
            ),
            dispatch = { intent ->
                when (intent) {
                    is CartFittingSheetIntent.ConfirmClick -> {
                        when (intent.option) {
                            CartFittingSheetOption.AllProducts -> {
                                dispatch(CartIntent.ConfirmFittingSheet(state.fittingProducts.map { product -> product.id }))
                            }
                            CartFittingSheetOption.PaymentProducts -> {
                                dispatch(
                                    CartIntent.ConfirmFittingSheet(
                                        state.fittingPaymentProducts.map { product -> product.id }
                                    )
                                )
                            }
                            CartFittingSheetOption.Manual -> dispatch(CartIntent.ShowFittingProductsSheet)
                        }
                    }
                    is CartFittingSheetIntent.DismissRequest -> {
                        dispatch(CartIntent.HideFittingSheet)
                    }
                }
            }
        )
    }

    if (state.isFittingProductsSheetVisible) {
        CartFittingProductsSheet(
            state = CartFittingProductsSheetModel(
                products = state.fittingProducts
            ),
            dispatch = { intent ->
                when (intent) {
                    is CartFittingProductsSheetIntent.ProductCheckedChange -> Unit
                    is CartFittingProductsSheetIntent.ConfirmClick -> {
                        dispatch(CartIntent.ConfirmFittingProductsSheet(intent.productIds))
                    }
                    is CartFittingProductsSheetIntent.DismissRequest -> {
                        dispatch(CartIntent.HideFittingProductsSheet)
                    }
                }
            }
        )
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
                        dispatch(CartIntent.ToggleSizePickerItem(intent.index))
                    }
                    is CartSizeSheetIntent.ConfirmClick -> {
                        dispatch(CartIntent.ConfirmSizePicker)
                    }
                    is CartSizeSheetIntent.DismissRequest -> {
                        dispatch(CartIntent.HideSizePicker)
                    }
                    is CartSizeSheetIntent.SizeTableClick -> {
                        dispatch(CartIntent.SizeTableClick)
                    }
                }
            }
        )
    }

    if (state.isColorPickerSheetVisible) {
        CartColorSheet(
            state = CartColorModel(
                colors = state.colorPickerColorsState
            ),
            dispatch = { intent ->
                when (intent) {
                    is CartColorIntent.ColorClick -> {
                        dispatch(CartIntent.ToggleColorPickerItem(intent.index))
                    }
                    is CartColorIntent.ConfirmClick -> {
                        dispatch(CartIntent.ConfirmColorPicker)
                    }
                    is CartColorIntent.DismissRequest -> {
                        dispatch(CartIntent.HideColorPicker)
                    }
                }
            }
        )
    }

    if (state.isQuantityPickerSheetVisible) {
        CartQuantitySheet(
            state = CartQuantityModel(
                quantities = state.quantityPickerValues
            ),
            dispatch = { intent ->
                when (intent) {
                    is CartQuantityIntent.QuantityClick -> {
                        dispatch(CartIntent.ToggleQuantityPickerItem(intent.index))
                    }
                    is CartQuantityIntent.ConfirmClick -> {
                        dispatch(CartIntent.ConfirmQuantityPicker)
                    }
                    is CartQuantityIntent.DismissRequest -> {
                        dispatch(CartIntent.HideQuantityPicker)
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
            CartChatDock(
                name = state.activeEmployee.employeeName,
                brand = state.cartChatBrand,
                onClick = { dispatch(CartIntent.ChatClick) }
            )
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
            isFittingSheetVisible = true,
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
