@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.cart

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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
import ru.mercury.vpclient.features.cart.fitting_sheet.CartFittingSheet
import ru.mercury.vpclient.features.cart.fitting_sheet.CartFittingSheetOption
import ru.mercury.vpclient.features.cart.navigation.CartPage
import ru.mercury.vpclient.features.cart.navigation.CartRoute
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedSnackbarHost
import ru.mercury.vpclient.shared.ui.components.SharedTabRow
import ru.mercury.vpclient.shared.ui.components.SharedTabRowState
import ru.mercury.vpclient.shared.ui.components.cart.CartActionButton
import ru.mercury.vpclient.shared.ui.components.cart.CartActionsBar
import ru.mercury.vpclient.shared.ui.components.cart.CartChatDock
import ru.mercury.vpclient.shared.ui.components.cart.CartEditProductSheet
import ru.mercury.vpclient.shared.ui.components.cart.CartFittingDeliveryHeader
import ru.mercury.vpclient.shared.ui.components.cart.CartFittingSummary
import ru.mercury.vpclient.shared.ui.components.cart.CartFittingProductsSheet
import ru.mercury.vpclient.shared.ui.components.cart.CartFittingSwitch
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
import kotlin.math.roundToInt

private const val CART_PAGE_COUNT = 2
private const val CART_PAGE_INDEX = 0
private const val FITTING_PAGE_INDEX = 1

@Composable
fun CartScreen(
    route: CartRoute = CartRoute(),
    viewModel: CartViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val snackbarHostStateError = remember { SnackbarHostState() }

    CartScreenContent(
        state = state,
        dispatch = viewModel::dispatch,
        snackbarHostStateError = snackbarHostStateError,
        initialPage = when (route.page) {
            CartPage.Cart -> CART_PAGE_INDEX
            CartPage.Fitting -> FITTING_PAGE_INDEX
        }
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
    snackbarHostStateError: SnackbarHostState,
    initialPage: Int = CART_PAGE_INDEX
) {
    val productBounds = remember { mutableStateMapOf<String, Rect>() }
    var draggingProductId by remember { mutableStateOf<String?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var dragPointerPosition by remember { mutableStateOf<Offset?>(null) }
    var contentBounds by remember { mutableStateOf<Rect?>(null) }
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { CART_PAGE_COUNT }
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
    val dragEnabled = state.payMode == CartPayMode.All && !state.isRefreshing
    val resetDrag = {
        draggingProductId = null
        dragOffset = Offset.Zero
        dragPointerPosition = null
    }

    LaunchedEffect(state.visibleProducts, state.visibleFittingProducts) {
        val visibleIds = (state.visibleProducts + state.visibleFittingProducts).map { product -> product.id }.toSet()
        productBounds.keys.retainAll(visibleIds)
    }

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

    if (state.isFittingSheetVisible) {
        CartFittingSheet(
            clientName = state.fittingSheetClientName,
            clientFeminine = state.isFittingSheetClientFeminine,
            allProductsCount = state.fittingProductsCount,
            allProductsSummary = state.fittingProductsSummary,
            paymentProductsCount = state.fittingPaymentProductsCount,
            paymentProductsSummary = state.fittingPaymentProductsSummary,
            hasProductsWithoutSize = state.hasProductsWithoutSize,
            onConfirmClick = { option ->
                when (option) {
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
            },
            onDismissRequest = { dispatch(CartIntent.HideFittingSheet) }
        )
    }

    if (state.isFittingProductsSheetVisible) {
        CartFittingProductsSheet(
            products = state.fittingProducts,
            onConfirmClick = { productIds -> dispatch(CartIntent.ConfirmFittingProductsSheet(productIds)) },
            onDismissRequest = { dispatch(CartIntent.HideFittingProductsSheet) }
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
                        when {
                            state.hasFittingProducts -> {
                                CartFittingSwitch(
                                    selectedIndex = pagerState.currentPage,
                                    onCartClick = { animateToPage(CART_PAGE_INDEX) },
                                    onFittingClick = { animateToPage(FITTING_PAGE_INDEX) }
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
                                modifier = Modifier.size(24.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )

            }
        },
        bottomBar = {
            CartChatDock(
                name = state.cartChatName,
                brand = state.cartChatBrand,
                onClick = { dispatch(CartIntent.ChatClick) }
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
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            userScrollEnabled = false
        ) { page ->
            when (page) {
                CART_PAGE_INDEX -> {
                    CartProductsPage(
                        state = state,
                        products = state.products,
                        visibleProducts = state.visibleProducts,
                        visibleProductGroups = state.visibleProductGroups,
                        allItemsCount = state.allItemsCount,
                        paymentItemsCount = state.paymentItemsCount,
                        showSummary = true,
                        showFittingSummary = false,
                        isFittingPage = false,
                        dragEnabled = dragEnabled,
                        draggingProductId = draggingProductId,
                        dragOffset = dragOffset,
                        productBounds = productBounds,
                        contentBounds = contentBounds,
                        onContentBoundsChange = { bounds -> contentBounds = bounds },
                        onDragStart = { productId, pointerPosition ->
                            draggingProductId = productId
                            dragOffset = Offset.Zero
                            dragPointerPosition = pointerPosition
                        },
                        onDrag = { dragAmount ->
                            dragOffset += dragAmount
                            dragPointerPosition = dragPointerPosition?.plus(dragAmount)
                        },
                        onDragEnd = { productId ->
                            dragTarget(
                                productId = productId,
                                pointerPosition = dragPointerPosition,
                                productBounds = productBounds
                            )?.let { target ->
                                dispatch(
                                    CartIntent.MoveProductAfterDrag(
                                        productId = productId,
                                        targetProductId = target.productId,
                                        placeAfterTarget = target.placeAfter
                                    )
                                )
                            }
                            resetDrag()
                        },
                        onDragCancel = resetDrag,
                        bottomBar = {
                            CartActionsBar(
                                onFittingClick = { dispatch(CartIntent.FittingClick) },
                                onBuyClick = { dispatch(CartIntent.BuyClick) },
                                isActionsEnabled = state.products.isNotEmpty()
                            )
                        },
                        dispatch = dispatch
                    )
                }
                FITTING_PAGE_INDEX -> {
                    CartProductsPage(
                        state = state,
                        products = state.apiFittingProducts,
                        visibleProducts = state.visibleFittingProducts,
                        visibleProductGroups = state.visibleFittingProductGroups,
                        allItemsCount = state.apiFittingProductsCount,
                        paymentItemsCount = state.apiFittingPaymentProductsCount,
                        showSummary = false,
                        showFittingSummary = true,
                        showFittingDeliveryHeader = true,
                        isFittingPage = true,
                        dragEnabled = false,
                        draggingProductId = draggingProductId,
                        dragOffset = dragOffset,
                        productBounds = productBounds,
                        contentBounds = contentBounds,
                        onContentBoundsChange = { bounds -> contentBounds = bounds },
                        onDragStart = { productId, pointerPosition ->
                            draggingProductId = productId
                            dragOffset = Offset.Zero
                            dragPointerPosition = pointerPosition
                        },
                        onDrag = { dragAmount ->
                            dragOffset += dragAmount
                            dragPointerPosition = dragPointerPosition?.plus(dragAmount)
                        },
                        onDragEnd = { _ -> resetDrag() },
                        onDragCancel = resetDrag,
                        bottomBar = {
                            CartBuyActionBar(
                                onClick = { dispatch(CartIntent.BuyClick) },
                                enabled = state.apiFittingProducts.isNotEmpty()
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
private fun CartProductsPage(
    state: CartModel,
    products: List<CartProduct>,
    visibleProducts: List<CartProduct>,
    visibleProductGroups: List<CartProductGroup>,
    allItemsCount: Int,
    paymentItemsCount: Int,
    showSummary: Boolean,
    showFittingSummary: Boolean,
    showFittingDeliveryHeader: Boolean = false,
    isFittingPage: Boolean,
    dragEnabled: Boolean,
    draggingProductId: String?,
    dragOffset: Offset,
    productBounds: MutableMap<String, Rect>,
    contentBounds: Rect?,
    onContentBoundsChange: (Rect) -> Unit,
    onDragStart: (String, Offset) -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: (String) -> Unit,
    onDragCancel: () -> Unit,
    bottomBar: @Composable () -> Unit,
    dispatch: (CartIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CartPageTabs(
                products = products,
                selectedPayMode = state.payMode,
                allItemsCount = allItemsCount,
                paymentItemsCount = paymentItemsCount,
                dispatch = dispatch
            )
        },
        bottomBar = bottomBar,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Horizontal)
    ) { innerPadding ->
        CartProductsPageContent(
            state = state,
            products = products,
            visibleProducts = visibleProducts,
            visibleProductGroups = visibleProductGroups,
            showSummary = showSummary,
            showFittingSummary = showFittingSummary,
            showFittingDeliveryHeader = showFittingDeliveryHeader,
            isFittingPage = isFittingPage,
            dragEnabled = dragEnabled,
            draggingProductId = draggingProductId,
            dragOffset = dragOffset,
            productBounds = productBounds,
            contentBounds = contentBounds,
            contentPadding = innerPadding,
            onContentBoundsChange = onContentBoundsChange,
            onDragStart = onDragStart,
            onDrag = onDrag,
            onDragEnd = onDragEnd,
            onDragCancel = onDragCancel,
            dispatch = dispatch
        )
    }
}

@Composable
private fun CartPageTabs(
    products: List<CartProduct>,
    selectedPayMode: CartPayMode,
    allItemsCount: Int,
    paymentItemsCount: Int,
    dispatch: (CartIntent) -> Unit
) {
    when {
        products.isEmpty() -> {
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
                    onFirstTabClick = { dispatch(CartIntent.SelectPayMode(CartPayMode.All)) },
                    onSecondTabClick = { dispatch(CartIntent.SelectPayMode(CartPayMode.Payment)) },
                    isLoading = false
                ),
                textStyle = MaterialTheme.typography.medium13,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
private fun CartProductsPageContent(
    state: CartModel,
    products: List<CartProduct>,
    visibleProducts: List<CartProduct>,
    visibleProductGroups: List<CartProductGroup>,
    showSummary: Boolean,
    showFittingSummary: Boolean,
    showFittingDeliveryHeader: Boolean,
    isFittingPage: Boolean,
    dragEnabled: Boolean,
    draggingProductId: String?,
    dragOffset: Offset,
    productBounds: MutableMap<String, Rect>,
    contentBounds: Rect?,
    contentPadding: PaddingValues,
    onContentBoundsChange: (Rect) -> Unit,
    onDragStart: (String, Offset) -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: (String) -> Unit,
    onDragCancel: () -> Unit,
    dispatch: (CartIntent) -> Unit
) {
    val pullToRefreshState = rememberPullToRefreshState()

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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .onGloballyPositioned { coordinates ->
                        onContentBoundsChange(coordinates.boundsInWindow())
                    }
            ) {
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
                                .padding(top = contentPadding.calculateTopPadding())
                        )
                    }
                ) {
                    SharedLazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = contentPadding,
                        verticalArrangement = Arrangement.spacedBy(
                            when (state.viewMode) {
                                CartViewMode.List -> 0.dp
                                CartViewMode.Cards -> 24.dp
                            }
                        )
                    ) {
                        when {
                            isFittingPage && showFittingDeliveryHeader -> {
                                state.visibleFittingDeliveryGroups.forEachIndexed { deliveryIndex, deliveryGroup ->
                                    item(
                                        key = "fitting_delivery_header_${deliveryIndex}_${deliveryGroup.header.date}_${deliveryGroup.header.address}"
                                    ) {
                                        val productIds = deliveryGroup.productGroups
                                            .flatMap { group -> group.products }
                                            .map { product -> product.id }

                                        if (deliveryIndex > 0) {
                                            CartProductDivider()
                                        }

                                        CartFittingDeliveryHeader(
                                            header = deliveryGroup.header,
                                            onClick = {
                                                dispatch(
                                                    CartIntent.FittingDeliveryClick(
                                                        productIds = productIds,
                                                        deliveryId = deliveryGroup.id,
                                                        fittingType = deliveryGroup.fittingType
                                                    )
                                                )
                                            }
                                        )

                                        CartProductDivider()
                                    }

                                    itemsIndexed(
                                        items = deliveryGroup.productGroups,
                                        key = { _, group -> "${deliveryIndex}_${group.key}" }
                                    ) { index, group ->
                                        Box(
                                            modifier = Modifier.zIndex(
                                                when {
                                                    group.products.any { product -> product.id == draggingProductId } -> 1F
                                                    else -> 0F
                                                }
                                            )
                                        ) {
                                            CartProductGroupItem(
                                                group = group,
                                                viewMode = state.viewMode,
                                                selectedAlternativeId = state.selectedAlternativeId,
                                                productModifier = { product ->
                                                    Modifier.cartProductDrag(
                                                        product = product,
                                                        enabled = dragEnabled,
                                                        productBounds = productBounds,
                                                        draggingProductId = draggingProductId,
                                                        onDragStart = onDragStart,
                                                        onDrag = onDrag,
                                                        onDragEnd = onDragEnd,
                                                        onDragCancel = onDragCancel
                                                    )
                                                },
                                                isFittingPage = isFittingPage,
                                                dispatch = dispatch
                                            )
                                        }

                                        val product = group.products.firstOrNull()
                                        val isAlternativesVisible = product?.isAlternativesPaletteOpen == true &&
                                            product.alternatives.isNotEmpty()
                                        val isDividerVisible = state.viewMode == CartViewMode.List &&
                                            !group.isLook &&
                                            index < deliveryGroup.productGroups.lastIndex &&
                                            !isAlternativesVisible

                                        if (isDividerVisible) {
                                            CartProductDivider()
                                        }
                                    }
                                }
                            }
                            else -> {
                                itemsIndexed(
                                    items = visibleProductGroups,
                                    key = { _, group -> group.key }
                                ) { index, group ->
                                    Box(
                                        modifier = Modifier.zIndex(
                                            when {
                                                group.products.any { product -> product.id == draggingProductId } -> 1F
                                                else -> 0F
                                            }
                                        )
                                    ) {
                                        CartProductGroupItem(
                                            group = group,
                                            viewMode = state.viewMode,
                                            selectedAlternativeId = state.selectedAlternativeId,
                                            productModifier = { product ->
                                                Modifier.cartProductDrag(
                                                    product = product,
                                                    enabled = dragEnabled,
                                                    productBounds = productBounds,
                                                    draggingProductId = draggingProductId,
                                                    onDragStart = onDragStart,
                                                    onDrag = onDrag,
                                                    onDragEnd = onDragEnd,
                                                    onDragCancel = onDragCancel
                                                )
                                            },
                                            isFittingPage = isFittingPage,
                                            dispatch = dispatch
                                        )
                                    }

                                    val product = group.products.firstOrNull()
                                    val isAlternativesVisible = product?.isAlternativesPaletteOpen == true &&
                                        product.alternatives.isNotEmpty()
                                    val isDividerVisible = state.viewMode == CartViewMode.List &&
                                        !group.isLook &&
                                        index < visibleProductGroups.lastIndex &&
                                        !isAlternativesVisible

                                    if (isDividerVisible) {
                                        CartProductDivider()
                                    }
                                }
                            }
                        }

                        if (showSummary) {
                            item {
                                CartProductDivider()

                                CartSummary(
                                    state = state
                                )
                            }
                        }

                        if (showFittingSummary) {
                            item {
                                if (showFittingDeliveryHeader) {
                                    CartProductDivider()
                                }

                                CartFittingSummary(
                                    fittingSummary = state.apiFittingProductsSummary,
                                    paymentSummary = state.apiFittingPaymentProductsSummary
                                )
                            }
                        }
                    }
                }

                CartDraggedProductOverlay(
                    product = draggingProductId?.let { productId ->
                        visibleProducts.firstOrNull { product -> product.id == productId }
                    },
                    viewMode = state.viewMode,
                    contentBounds = contentBounds,
                    productBounds = draggingProductId?.let { productId -> productBounds[productId] },
                    dragOffset = dragOffset
                )
            }
        }
    }
}

@Composable
private fun CartBuyActionBar(
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

@Composable
private fun CartDraggedProductOverlay(
    product: CartProduct?,
    viewMode: CartViewMode,
    contentBounds: Rect?,
    productBounds: Rect?,
    dragOffset: Offset
) {
    if (product == null || contentBounds == null || productBounds == null) {
        return
    }

    val density = LocalDensity.current
    val modifier = Modifier
        .width(with(density) { productBounds.width.toDp() })
        .offset {
            IntOffset(
                x = (productBounds.left - contentBounds.left).roundToInt(),
                y = (productBounds.top - contentBounds.top + dragOffset.y).roundToInt()
            )
        }
        .zIndex(10F)
        .graphicsLayer {
            alpha = .96F
            shadowElevation = 6F
        }

    when (viewMode) {
        CartViewMode.List -> {
            CartProductCard(
                product = product,
                modifier = modifier
            )
        }
        CartViewMode.Cards -> {
            CartProductLargeCard(
                product = product,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun CartProductGroupItem(
    group: CartProductGroup,
    viewMode: CartViewMode,
    selectedAlternativeId: String?,
    productModifier: (CartProduct) -> Modifier,
    isFittingPage: Boolean,
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
                    when {
                        isFittingPage -> dispatch(CartIntent.ChangeFittingPaySwitch(product, paySwitch))
                        else -> dispatch(CartIntent.ChangePaySwitch(product, paySwitch))
                    }
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
                productModifier = productModifier,
                selectedAlternativeId = selectedAlternativeId
            )
        }
        else -> {
            val product = group.products.first()
            when (viewMode) {
                CartViewMode.List -> {
                    CartProductCard(
                        product = product,
                        modifier = productModifier(product),
                        onClick = { dispatch(CartIntent.ProductClick(product.detailId)) },
                        onSelectSizeClick = { dispatch(CartIntent.ShowSizePicker(product)) },
                        onBuySwitchChange = { paySwitch ->
                            when {
                                isFittingPage -> dispatch(CartIntent.ChangeFittingPaySwitch(product, paySwitch))
                                else -> dispatch(CartIntent.ChangePaySwitch(product, paySwitch))
                            }
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
                        modifier = productModifier(product),
                        onClick = { dispatch(CartIntent.ProductClick(product.detailId)) },
                        onSelectSizeClick = { dispatch(CartIntent.ShowSizePicker(product)) },
                        onBuySwitchChange = { paySwitch ->
                            when {
                                isFittingPage -> dispatch(CartIntent.ChangeFittingPaySwitch(product, paySwitch))
                                else -> dispatch(CartIntent.ChangePaySwitch(product, paySwitch))
                            }
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

private data class CartDragTarget(
    val productId: String,
    val placeAfter: Boolean
)

private fun Modifier.cartProductDrag(
    product: CartProduct,
    enabled: Boolean,
    productBounds: MutableMap<String, Rect>,
    draggingProductId: String?,
    onDragStart: (String, Offset) -> Unit,
    onDrag: (Offset) -> Unit,
    onDragEnd: (String) -> Unit,
    onDragCancel: () -> Unit
): Modifier {
    val isDragging = draggingProductId == product.id
    val dragModifier = when {
        enabled -> {
            Modifier.pointerInput(product.id) {
                detectDragGesturesAfterLongPress(
                    onDragStart = { touchOffset ->
                        productBounds[product.id]?.let { bounds ->
                            onDragStart(
                                product.id,
                                Offset(
                                    x = bounds.left + touchOffset.x,
                                    y = bounds.top + touchOffset.y
                                )
                            )
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onDrag(dragAmount)
                    },
                    onDragEnd = { onDragEnd(product.id) },
                    onDragCancel = onDragCancel
                )
            }
        }
        else -> Modifier
    }

    return this
        .zIndex(if (isDragging) 1F else 0F)
        .graphicsLayer {
            alpha = if (isDragging) .18F else 1F
        }
        .onGloballyPositioned { coordinates ->
            productBounds[product.id] = coordinates.boundsInWindow()
        }
        .then(dragModifier)
}

private fun dragTarget(
    productId: String,
    pointerPosition: Offset?,
    productBounds: Map<String, Rect>
): CartDragTarget? {
    val pointer = pointerPosition ?: return null
    val target = productBounds.entries.firstOrNull { entry ->
        entry.key != productId && entry.value.contains(pointer)
    } ?: return null

    return CartDragTarget(
        productId = target.key,
        placeAfter = pointer.y > target.value.center.y
    )
}
