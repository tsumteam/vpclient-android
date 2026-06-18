@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.cart_quantity_sheet

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.cart_quantity_sheet.intent.CartQuantityIntent
import ru.mercury.vpclient.features.cart_quantity_sheet.model.CartQuantityItem
import ru.mercury.vpclient.features.cart_quantity_sheet.model.CartQuantityModel
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular18
import kotlin.math.abs

@Composable
fun CartQuantitySheet(
    state: CartQuantityModel,
    dispatch: (CartQuantityIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val sheetDispatch: (CartQuantityIntent) -> Unit = { intent ->
        when (intent) {
            is CartQuantityIntent.QuantityClick -> dispatch(intent)
            is CartQuantityIntent.ConfirmClick,
            is CartQuantityIntent.DismissRequest -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(CartQuantityIntent.DismissRequest) },
        sheetState = sheetState
    ) {
        val selectedIndex = state.quantities.indexOfFirst { it.selected }.coerceAtLeast(0)
        val listState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)
        val scope = rememberCoroutineScope()
        val density = LocalDensity.current
        val rowHeightPx = with(density) { 52.dp.roundToPx() }
        val cameraDistance = with(density) { 12.dp.toPx() }

        LaunchedEffect(state.quantities.size) {
            if (state.quantities.isNotEmpty()) {
                listState.scrollToItem(selectedIndex)
            }
        }

        LaunchedEffect(listState, rowHeightPx, state.quantities.size) {
            snapshotFlow { listState.isScrollInProgress }
                .distinctUntilChanged()
                .collect { isScrollInProgress ->
                    if (!isScrollInProgress && state.quantities.isNotEmpty()) {
                        val centeredIndex = when {
                            listState.firstVisibleItemScrollOffset > rowHeightPx / 2 -> {
                                listState.firstVisibleItemIndex + 1
                            }
                            else -> listState.firstVisibleItemIndex
                        }.coerceIn(0, state.quantities.lastIndex)

                        sheetDispatch(CartQuantityIntent.QuantityClick(centeredIndex))
                        listState.animateScrollToItem(centeredIndex)
                    }
                }
        }

        Column {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.CartEditChangeQuantityCaps),
                        style = MaterialTheme.typography.livretMedium18.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 26.sp,
                            letterSpacing = .2.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { sheetDispatch(CartQuantityIntent.DismissRequest) }
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

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(156.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                        .height(52.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainer,
                            shape = RoundedCornerShape(2.dp)
                        )
                )

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(156.dp),
                    contentPadding = PaddingValues(vertical = 52.dp)
                ) {
                    itemsIndexed(
                        items = state.quantities,
                        key = { _, item -> item.value }
                    ) { index, item ->
                        val textColor by animateColorAsState(
                            targetValue = when {
                                item.selected -> MaterialTheme.colorScheme.onBackground
                                else -> MaterialTheme.colorScheme.outline
                            },
                            label = "CartQuantityTextColor"
                        )

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .fillMaxWidth()
                                .height(52.dp)
                                .graphicsLayer {
                                    val itemInfo = listState.layoutInfo.visibleItemsInfo.firstOrNull {
                                        it.index == index
                                    }
                                    val viewportCenter = (
                                        listState.layoutInfo.viewportStartOffset +
                                            listState.layoutInfo.viewportEndOffset
                                        ) / 2f
                                    val itemCenter = itemInfo?.let {
                                        it.offset + it.size / 2f
                                    } ?: viewportCenter
                                    val distanceFromCenter = ((itemCenter - viewportCenter) / rowHeightPx)
                                        .coerceIn(-1.5f, 1.5f)
                                    val depth = abs(distanceFromCenter)

                                    rotationX = -distanceFromCenter * 28f
                                    scaleX = 1f - depth * .04f
                                    scaleY = 1f - depth * .08f
                                    alpha = 1f - depth * .18f
                                    this.cameraDistance = cameraDistance
                                }
                                .clickable {
                                    scope.launch {
                                        sheetDispatch(CartQuantityIntent.QuantityClick(index))
                                        listState.animateScrollToItem(index)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.value.toString(),
                                style = MaterialTheme.typography.regular18.copy(
                                    color = textColor,
                                    lineHeight = 24.sp
                                )
                            )
                        }
                    }
                }
            }

            Button(
                onClick = { sheetDispatch(CartQuantityIntent.ConfirmClick) },
                modifier = Modifier
                    .padding(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 8.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = state.hasSelectedQuantity,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    text = stringResource(ClientStrings.CartSave),
                    style = MaterialTheme.typography.medium15.copy(
                        textAlign = TextAlign.Center,
                        letterSpacing = .3.sp
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CartQuantitySheetPreview(
    @PreviewParameter(CartQuantitySheetModelProvider::class) state: CartQuantityModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CartQuantitySheet(
            state = state,
            dispatch = {}
        )
    }
}

private class CartQuantitySheetModelProvider: PreviewParameterProvider<CartQuantityModel> {
    override val values: Sequence<CartQuantityModel> = sequenceOf(
        CartQuantityModel(
            quantities = (1..10).map { value ->
                CartQuantityItem(
                    value = value,
                    selected = value == 2
                )
            }
        )
    )
}
