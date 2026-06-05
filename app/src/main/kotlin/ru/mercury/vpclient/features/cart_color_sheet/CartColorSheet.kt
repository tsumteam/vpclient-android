@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.cart_color_sheet

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import ru.mercury.vpclient.features.cart_color_sheet.intent.CartColorIntent
import ru.mercury.vpclient.features.cart_color_sheet.model.CartColorModel
import ru.mercury.vpclient.shared.data.entity.ProductAvailableColor
import ru.mercury.vpclient.shared.domain.mapper.colorFromHex
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular18
import kotlin.math.abs

@Composable
fun CartColorSheet(
    state: CartColorModel,
    dispatch: (CartColorIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val sheetDispatch: (CartColorIntent) -> Unit = { intent ->
        when (intent) {
            is CartColorIntent.ColorClick -> dispatch(intent)
            is CartColorIntent.ConfirmClick,
            is CartColorIntent.DismissRequest -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(CartColorIntent.DismissRequest) },
        sheetState = sheetState
    ) {
        val selectedIndex = state.colors.indexOfFirst { it.selected }.coerceAtLeast(0)
        val listState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)
        val density = LocalDensity.current
        val rowHeightPx = with(density) { 52.dp.roundToPx() }
        val cameraDistance = with(density) { 12.dp.toPx() }

        LaunchedEffect(state.colors.size) {
            if (state.colors.isNotEmpty()) {
                listState.scrollToItem(selectedIndex)
            }
        }

        LaunchedEffect(listState, rowHeightPx, state.colors.size) {
            snapshotFlow { listState.isScrollInProgress }
                .distinctUntilChanged()
                .collect { isScrollInProgress ->
                    if (!isScrollInProgress && state.colors.isNotEmpty()) {
                        val centeredIndex = when {
                            listState.firstVisibleItemScrollOffset > rowHeightPx / 2 -> {
                                listState.firstVisibleItemIndex + 1
                            }
                            else -> listState.firstVisibleItemIndex
                        }.coerceIn(0, state.colors.lastIndex)

                        sheetDispatch(CartColorIntent.ColorClick(centeredIndex))
                        listState.animateScrollToItem(centeredIndex)
                    }
                }
        }

        Column {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(ClientStrings.CartFittingSelectColorCaps),
                        style = MaterialTheme.typography.livretMedium18.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            lineHeight = 26.sp,
                            letterSpacing = .2.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { sheetDispatch(CartColorIntent.DismissRequest) }
                    ) {
                        Icon(
                            imageVector = Close24,
                            contentDescription = null,
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
                        items = state.colors,
                        key = { _, color -> color.id }
                    ) { index, color ->
                        val swatchColor = color.hex.colorFromHex(MaterialTheme.colorScheme.outlineVariant)
                        val cleanHex = color.hex.removePrefix("#").uppercase()
                        val isWhiteColor = cleanHex == "FFFFFF" || cleanHex == "FFFFFFFF"
                        val textColor by animateColorAsState(
                            targetValue = when {
                                color.selected -> MaterialTheme.colorScheme.onBackground
                                else -> MaterialTheme.colorScheme.outline
                            },
                            label = "CartColorTextColor"
                        )

                        Row(
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
                                        sheetDispatch(CartColorIntent.ColorClick(index))
                                        listState.animateScrollToItem(index)
                                    }
                                },
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(swatchColor)
                                    .then(
                                        when {
                                            isWhiteColor -> Modifier.border(1.dp, Color.Black, CircleShape)
                                            else -> Modifier
                                        }
                                    )
                            )

                            Text(
                                text = color.name,
                                modifier = Modifier.padding(start = 8.dp),
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
                onClick = { sheetDispatch(CartColorIntent.ConfirmClick) },
                modifier = Modifier
                    .padding(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 8.dp)
                    .fillMaxWidth()
                    .height(52.dp),
                enabled = state.hasSelectedColor,
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
private fun CartColorSheetPreview(
    @PreviewParameter(CartColorModelPreviewParameterProvider::class) state: CartColorModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CartColorSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class CartColorModelPreviewParameterProvider: PreviewParameterProvider<CartColorModel> {
    override val values: Sequence<CartColorModel> = sequenceOf(
        CartColorModel(
            colors = listOf(
                ProductAvailableColor(id = "red", name = "Красный", hex = "#F1C8C8"),
                ProductAvailableColor(id = "brown", name = "Коричневый", hex = "#783C00", selected = true),
                ProductAvailableColor(id = "green", name = "Зеленый", hex = "#C2EBD7")
            )
        )
    )
}
