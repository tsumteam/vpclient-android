@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.cart_fitting_color_picker_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import ru.mercury.vpclient.features.cart_fitting_color_picker_sheet.intent.CartFittingColorPickerSheetIntent
import ru.mercury.vpclient.features.cart_fitting_color_picker_sheet.model.CartFittingColorPickerSheetModel
import ru.mercury.vpclient.shared.data.entity.ProductAvailableColor
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.disabled
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.medium18
import ru.mercury.vpclient.shared.ui.theme.onDisabled
import ru.mercury.vpclient.shared.ui.theme.regular18

@Composable
fun CartFittingColorPickerSheet(
    state: CartFittingColorPickerSheetModel,
    dispatch: (CartFittingColorPickerSheetIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val sheetDispatch: (CartFittingColorPickerSheetIntent) -> Unit = { intent ->
        when (intent) {
            is CartFittingColorPickerSheetIntent.ColorClick -> dispatch(intent)
            is CartFittingColorPickerSheetIntent.ConfirmClick,
            is CartFittingColorPickerSheetIntent.DismissRequest -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(CartFittingColorPickerSheetIntent.DismissRequest) },
        sheetState = sheetState
    ) {
        CartFittingColorPickerSheetContent(
            state = state,
            dispatch = sheetDispatch
        )
    }
}

@Composable
private fun CartFittingColorPickerSheetContent(
    state: CartFittingColorPickerSheetModel,
    dispatch: (CartFittingColorPickerSheetIntent) -> Unit
) {
    val selectedIndex = state.colors.indexOfFirst { it.selected }.coerceAtLeast(0)
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)
    val scope = rememberCoroutineScope()
    val rowHeightPx = with(LocalDensity.current) { 52.dp.roundToPx() }

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

                    dispatch(CartFittingColorPickerSheetIntent.ColorClick(centeredIndex))
                    listState.animateScrollToItem(centeredIndex)
                }
            }
    }

    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(ClientStrings.CartFittingSelectColorCaps),
                    style = MaterialTheme.typography.medium18.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { dispatch(CartFittingColorPickerSheetIntent.DismissRequest) }
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
                    CartFittingColorRow(
                        color = color,
                        onClick = {
                            scope.launch {
                                dispatch(CartFittingColorPickerSheetIntent.ColorClick(index))
                                listState.animateScrollToItem(index)
                            }
                        }
                    )
                }
            }
        }

        Button(
            onClick = { dispatch(CartFittingColorPickerSheetIntent.ConfirmClick) },
            modifier = Modifier
                .padding(start = 16.dp, top = 28.dp, end = 16.dp, bottom = 8.dp)
                .fillMaxWidth()
                .height(52.dp),
            enabled = state.hasSelectedColor,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.disabled,
                disabledContentColor = MaterialTheme.colorScheme.onDisabled
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

@Composable
private fun CartFittingColorRow(
    color: ProductAvailableColor,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .height(52.dp)
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(color.hex.colorFromHex(MaterialTheme.colorScheme.outlineVariant))
        )

        Text(
            text = color.name,
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.regular18.copy(
                color = when {
                    color.selected -> MaterialTheme.colorScheme.onBackground
                    else -> MaterialTheme.colorScheme.outline
                },
                lineHeight = 24.sp
            )
        )
    }
}

private fun String.colorFromHex(fallback: Color): Color {
    val cleanHex = removePrefix("#")
    return when (cleanHex.length) {
        6 -> runCatching { Color(("FF$cleanHex").toLong(16)) }.getOrDefault(fallback)
        8 -> runCatching { Color(cleanHex.toLong(16)) }.getOrDefault(fallback)
        else -> fallback
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartFittingColorPickerSheetContentPreview(
    @PreviewParameter(CartFittingColorPickerSheetModelProvider::class) state: CartFittingColorPickerSheetModel
) {
    CartFittingColorPickerSheetContent(
        state = state,
        dispatch = {}
    )
}

private class CartFittingColorPickerSheetModelProvider: PreviewParameterProvider<CartFittingColorPickerSheetModel> {
    override val values: Sequence<CartFittingColorPickerSheetModel> = sequenceOf(
        CartFittingColorPickerSheetModel(
            colors = listOf(
                ProductAvailableColor(id = "red", name = "Красный", hex = "#F1C8C8"),
                ProductAvailableColor(id = "brown", name = "Коричневый", hex = "#783C00", selected = true),
                ProductAvailableColor(id = "green", name = "Зеленый", hex = "#C2EBD7")
            )
        )
    )
}
