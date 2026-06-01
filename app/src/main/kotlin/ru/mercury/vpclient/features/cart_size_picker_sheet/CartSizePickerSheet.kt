@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.cart_size_picker_sheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.cart_size_picker_sheet.intent.CartSizePickerSheetIntent
import ru.mercury.vpclient.features.cart_size_picker_sheet.model.CartSizePickerSheetModel
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.cart.CartSizePickerLoading
import ru.mercury.vpclient.shared.ui.components.details.DetailsSizeSelector
import ru.mercury.vpclient.shared.ui.components.details.SizeState
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.disabled
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.onDisabled

data class SizeSelectorState(
    val sizes: List<SizeState>,
    val topText: String,
    val bottomText: String,
    val isSizeTableVisible: Boolean
) {
    companion object {
        val Empty = SizeSelectorState(
            sizes = emptyList(),
            topText = "",
            bottomText = "",
            isSizeTableVisible = false
        )
    }
}

@Composable
fun CartSizePickerSheet(
    state: CartSizePickerSheetModel,
    dispatch: (CartSizePickerSheetIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val sheetDispatch: (CartSizePickerSheetIntent) -> Unit = { intent ->
        when (intent) {
            is CartSizePickerSheetIntent.ConfirmClick,
            is CartSizePickerSheetIntent.DismissRequest -> {
                scope.launch {
                    sheetState.hide()
                    dispatch(intent)
                }
            }
            is CartSizePickerSheetIntent.SizeClick -> dispatch(intent)
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(CartSizePickerSheetIntent.DismissRequest) },
        sheetState = sheetState
    ) {
        CartSizePickerSheetContent(
            state = state,
            dispatch = sheetDispatch
        )
    }
}

@Composable
fun CartSizePickerSheetContent(
    state: CartSizePickerSheetModel,
    dispatch: (CartSizePickerSheetIntent) -> Unit
) {
    Column {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(ClientStrings.CartSelectSizeCaps),
                    style = MaterialTheme.typography.medium15.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = { dispatch(CartSizePickerSheetIntent.DismissRequest) }
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
        when (state.sizeSelectorState) {
            SizeSelectorState.Empty -> CartSizePickerLoading()
            else -> {
                DetailsSizeSelector(
                    state = state.sizeSelectorState,
                    onSizeClick = { index -> dispatch(CartSizePickerSheetIntent.SizeClick(index)) },
                    onSizeTableClick = {}
                )
            }
        }

        Button(
            onClick = { dispatch(CartSizePickerSheetIntent.ConfirmClick) },
            modifier = Modifier
                .padding(start = 16.dp, top = 28.dp, end = 16.dp, bottom = 8.dp)
                .fillMaxWidth()
                .height(52.dp)
                .placeholder(
                    visible = state.sizeSelectorState == SizeSelectorState.Empty,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                ),
            enabled = state.sizeSelectorState.sizes.any { it.selected },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.disabled,
                disabledContentColor = MaterialTheme.colorScheme.onDisabled
            )
        ) {
            Text(
                text = state.buttonText,
                style = MaterialTheme.typography.medium15.copy(
                    textAlign = TextAlign.Center,
                    letterSpacing = .3.sp
                )
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartSizePickerSheetContentPreview(
    @PreviewParameter(CartSizePickerSheetSizeSelectorStateProvider::class) state: SizeSelectorState
) {
    CartSizePickerSheetContent(
        state = CartSizePickerSheetModel(
            sizeSelectorState = state,
            buttonText = stringResource(ClientStrings.CartSelectSizeForPaymentButton)
        ),
        dispatch = {}
    )
}

private class CartSizePickerSheetSizeSelectorStateProvider: PreviewParameterProvider<SizeSelectorState> {

    private val sizes = listOf(
        SizeState(topText = "RU 36", bottomText = "IT 34", selected = false, enabled = true),
        SizeState(topText = "RU 38", bottomText = "IT 36", selected = false, enabled = true),
        SizeState(topText = "RU 40", bottomText = "IT 38", selected = false, enabled = true),
        SizeState(topText = "RU 42", bottomText = "IT 40", selected = false, enabled = false),
        SizeState(topText = "RU 44", bottomText = "IT 42", selected = false, enabled = true),
        SizeState(topText = "RU 46", bottomText = "IT 44", selected = false, enabled = false)
    )

    override val values: Sequence<SizeSelectorState> = sequenceOf(
        SizeSelectorState.Empty,
        SizeSelectorState(
            sizes = sizes,
            topText = "IT",
            bottomText = "RU",
            isSizeTableVisible = true
        ),
        SizeSelectorState(
            sizes = sizes.mapIndexed { index, state -> state.copy(selected = index == 1) },
            topText = "IT",
            bottomText = "RU",
            isSizeTableVisible = true
        ),
        SizeSelectorState(
            sizes = sizes.mapIndexed { index, state -> state.copy(selected = index == 3) },
            topText = "IT",
            bottomText = "RU",
            isSizeTableVisible = true
        )
    )
}
