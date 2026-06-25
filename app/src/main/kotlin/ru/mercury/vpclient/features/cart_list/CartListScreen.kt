package ru.mercury.vpclient.features.cart_list

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import ru.mercury.vpclient.features.cart.intent.CartIntent
import ru.mercury.vpclient.features.cart.model.CartModel
import ru.mercury.vpclient.shared.data.entity.CartPayMode
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductGroup
import ru.mercury.vpclient.shared.domain.mapper.moveProductAfterDrag
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedPullToRefreshBox
import ru.mercury.vpclient.shared.ui.components.SharedScaffold
import ru.mercury.vpclient.shared.ui.components.SharedTabRow
import ru.mercury.vpclient.shared.ui.components.SharedTabRowState
import ru.mercury.vpclient.shared.ui.components.cart.CartLookCard
import ru.mercury.vpclient.shared.ui.components.cart.CartProductCard
import ru.mercury.vpclient.shared.ui.components.cart.CartProductCardState
import ru.mercury.vpclient.shared.ui.components.cart.CartSummary
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.divider
import ru.mercury.vpclient.shared.ui.theme.medium13
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular14
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun CartListScreen(
    state: CartModel,
    dispatch: (CartIntent) -> Unit
) {
    CartListScreenContent(
        state = state,
        dispatch = dispatch
    )
}

@Composable
private fun CartListScreenContent(
    state: CartModel,
    dispatch: (CartIntent) -> Unit
) {
    val productBounds = remember { mutableStateMapOf<String, Rect>() }
    var draggingProductId by remember { mutableStateOf<String?>(null) }
    var draggingProducts by remember { mutableStateOf<List<CartProduct>?>(null) }
    var dragTargetProductId by remember { mutableStateOf<String?>(null) }
    var dragPlaceAfterTarget by remember { mutableStateOf(false) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var dragPointerPosition by remember { mutableStateOf<Offset?>(null) }
    var contentBounds by remember { mutableStateOf<Rect?>(null) }
    var openedSwipeKey by remember { mutableStateOf<String?>(null) }
    val lazyListState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()
    val density = LocalDensity.current
    val resetDrag = {
        draggingProductId = null
        draggingProducts = null
        dragTargetProductId = null
        dragPlaceAfterTarget = false
        dragOffset = Offset.Zero
        dragPointerPosition = null
    }

    LaunchedEffect(state.visibleProducts) {
        val visibleIds = state.visibleProducts.map { product -> product.id }.toSet()
        productBounds.keys.retainAll(visibleIds)
    }

    SharedScaffold(
        topBar = {
            when {
                state.products.isEmpty() -> {
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
        floatingActionButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { dispatch(CartIntent.FittingClick) },
                    enabled = state.products.isNotEmpty(),
                    modifier = Modifier
                        .weight(2F)
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(ClientStrings.CartFitting),
                        style = MaterialTheme.typography.medium15.copy(
                            letterSpacing = .3.sp
                        )
                    )
                }

                Button(
                    onClick = { dispatch(CartIntent.BuyClick) },
                    enabled = state.products.isNotEmpty(),
                    modifier = Modifier
                        .weight(1F)
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
            }
        },
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.only(WindowInsetsSides.Horizontal)
    ) { innerPadding ->
        val displayState = draggingProducts?.let { products -> state.copy(products = products) } ?: state
        val autoScrollEdgePx = with(density) { 80.dp.toPx() }
        val autoScrollStepPx = with(density) { 28.dp.toPx() }
        val moveDraggingProductOverTarget: (String, Offset) -> Unit = { productId, pointer ->
            val target = productBounds.entries.firstOrNull { entry ->
                entry.key != productId && entry.value.contains(pointer)
            }

            if (target != null) {
                val placeAfterTarget = pointer.y > target.value.center.y
                val movedProducts = (draggingProducts ?: state.products).moveProductAfterDrag(
                    productId = productId,
                    targetProductId = target.key,
                    placeAfterTarget = placeAfterTarget
                )
                when (movedProducts) {
                    draggingProducts -> Unit
                    else -> {
                        draggingProducts = movedProducts
                        dragTargetProductId = target.key
                        dragPlaceAfterTarget = placeAfterTarget
                    }
                }
            }
        }

        LaunchedEffect(draggingProductId, dragPointerPosition, contentBounds) {
            while (draggingProductId != null) {
                val productId = draggingProductId
                val pointer = dragPointerPosition
                val currentContentBounds = contentBounds
                val scrollAmount = when {
                    pointer == null || currentContentBounds == null -> 0F
                    pointer.y < currentContentBounds.top + autoScrollEdgePx -> -autoScrollStepPx
                    pointer.y > currentContentBounds.bottom - autoScrollEdgePx -> autoScrollStepPx
                    else -> 0F
                }
                if (scrollAmount != 0F) {
                    lazyListState.scrollBy(scrollAmount)
                }
                if (productId != null && pointer != null) {
                    moveDraggingProductOverTarget(productId, pointer)
                }
                delay(16.milliseconds)
            }
        }

        val productModifier: (CartProduct) -> Modifier = { product ->
            val isDragging = draggingProductId == product.id
            val dragModifier = when {
                state.dragEnabled -> {
                    Modifier.pointerInput(product.id) {
                        detectDragGesturesAfterLongPress(
                            onDragStart = { touchOffset ->
                                productBounds[product.id]?.let { bounds ->
                                    draggingProductId = product.id
                                    draggingProducts = state.products
                                    dragTargetProductId = null
                                    dragPlaceAfterTarget = false
                                    dragOffset = Offset.Zero
                                    dragPointerPosition = Offset(
                                        x = bounds.left + touchOffset.x,
                                        y = bounds.top + touchOffset.y
                                    )
                                }
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                dragOffset += dragAmount
                                val pointer = dragPointerPosition?.plus(dragAmount)
                                dragPointerPosition = pointer

                                if (pointer != null) {
                                    moveDraggingProductOverTarget(product.id, pointer)
                                }
                            },
                            onDragEnd = {
                                val targetProductId = dragTargetProductId
                                if (targetProductId != null) {
                                    dispatch(
                                        CartIntent.MoveProductAfterDrag(
                                            productId = product.id,
                                            targetProductId = targetProductId,
                                            placeAfterTarget = dragPlaceAfterTarget
                                        )
                                    )
                                }
                                resetDrag()
                            },
                            onDragCancel = resetDrag
                        )
                    }
                }
                else -> Modifier
            }

            Modifier
                .zIndex(if (isDragging) 1F else 0F)
                .graphicsLayer {
                    alpha = if (isDragging) 0F else 1F
                }
                .onGloballyPositioned { coordinates ->
                    productBounds[product.id] = coordinates.boundsInWindow()
                }
                .then(dragModifier)
        }
        val renderProductGroup: @Composable (CartProductGroup, Int, Int) -> Unit = { group, groupIndex, lastGroupIndex ->
            Box(
                modifier = Modifier.zIndex(
                    when {
                        group.products.any { product -> product.id == draggingProductId } -> 1F
                        else -> 0F
                    }
                )
            ) {
                when {
                    group.isLook -> {
                        CartLookCard(
                            lookName = group.lookName,
                            lookImageUrl = group.lookImageUrl,
                            products = group.products,
                            onAddClick = {},
                            onProductClick = { product -> dispatch(CartIntent.ProductClick(product.detailId)) },
                            onSelectSizeClick = { product -> dispatch(CartIntent.ShowSizePicker(product)) },
                            onSizeClick = { product, size -> dispatch(CartIntent.RemoveProductSizeClick(product, size)) },
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
                            productModifier = productModifier,
                            swipeKey = "look_${group.key}",
                            openedSwipeKey = openedSwipeKey,
                            onSwipeOpen = { key -> openedSwipeKey = key },
                            selectedAlternativeId = state.selectedAlternativeId
                        )
                    }
                    else -> {
                        val product = group.products.first()

                        CartProductCard(
                            state = CartProductCardState(
                                product = product,
                                onClick = { dispatch(CartIntent.ProductClick(product.detailId)) },
                                onSelectSizeClick = { dispatch(CartIntent.ShowSizePicker(product)) },
                                onSizeClick = { size -> dispatch(CartIntent.RemoveProductSizeClick(product, size)) },
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
                                swipeKey = "product_${product.id}",
                                openedSwipeKey = openedSwipeKey,
                                onSwipeOpen = { key -> openedSwipeKey = key },
                                selectedAlternativeId = state.selectedAlternativeId
                            ),
                            modifier = productModifier(product)
                        )
                    }
                }
            }

            val product = group.products.firstOrNull()
            val isAlternativesVisible = product?.isAlternativesPaletteOpen == true && product.alternatives.isNotEmpty()
            val isDividerVisible = !group.isLook && groupIndex < lastGroupIndex && !isAlternativesVisible

            if (isDividerVisible) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.divider
                )
            }
        }

        when {
            state.products.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
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
                            contentBounds = coordinates.boundsInWindow()
                        }
                ) {
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
                            state = lazyListState,
                            contentPadding = innerPadding + PaddingValues(bottom = 76.dp),
                            verticalArrangement = Arrangement.spacedBy(0.dp)
                        ) {
                            itemsIndexed(
                                items = displayState.visibleProductGroups,
                                key = { _, group -> group.key }
                            ) { index, group ->
                                renderProductGroup(
                                    group,
                                    index,
                                    displayState.visibleProductGroups.lastIndex
                                )
                            }

                            item {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    color = MaterialTheme.colorScheme.divider
                                )

                                CartSummary(
                                    state = state
                                )
                            }
                        }
                    }

                    val draggedProduct = draggingProductId?.let { productId ->
                        displayState.visibleProducts.firstOrNull { product -> product.id == productId }
                    }
                    val draggedProductBounds = draggingProductId?.let { productId -> productBounds[productId] }
                    val currentContentBounds = contentBounds

                    if (draggedProduct != null && currentContentBounds != null && draggedProductBounds != null) {
                        val modifier = Modifier
                            .width(with(density) { draggedProductBounds.width.toDp() })
                            .offset {
                                IntOffset(
                                    x = (draggedProductBounds.left - currentContentBounds.left).roundToInt(),
                                    y = (draggedProductBounds.top - currentContentBounds.top + dragOffset.y).roundToInt()
                                )
                            }
                            .zIndex(10F)
                            .graphicsLayer {
                                alpha = .96F
                                shadowElevation = 6F
                            }

                        CartProductCard(
                            state = CartProductCardState(
                                product = draggedProduct
                            ),
                            modifier = modifier
                        )
                    }
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CartListScreenPreview(
    @PreviewParameter(CartListScreenModelProvider::class) state: CartModel
) {
    CartListScreenContent(
        state = state,
        dispatch = {}
    )
}

private class CartListScreenModelProvider: PreviewParameterProvider<CartModel> {
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
        CartModel(products = products)
    )
}
