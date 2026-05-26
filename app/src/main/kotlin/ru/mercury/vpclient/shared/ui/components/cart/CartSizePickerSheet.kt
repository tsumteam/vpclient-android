@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.DialogToolbar
import ru.mercury.vpclient.shared.ui.components.SharedButton
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.components.details.DetailsSizeSelector
import ru.mercury.vpclient.shared.ui.components.details.SizeState
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.SizeSelectorStateProvider
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
    state: SizeSelectorState,
    onSizeClick: (Int) -> Unit,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val dismiss: () -> Unit = {
        scope.launch {
            sheetState.hide()
            onDismissRequest()
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState
    ) {
        CartSizePickerSheetContent(
            state = state,
            onSizeClick = onSizeClick,
            onConfirmClick = {
                scope.launch {
                    sheetState.hide()
                    onConfirmClick()
                }
            },
            onCloseClick = dismiss
        )
    }
}

@Composable
fun CartSizePickerSheetContent(
    state: SizeSelectorState,
    onSizeClick: (Int) -> Unit,
    onConfirmClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        DialogToolbar(
            text = stringResource(ClientStrings.CartSelectSize).uppercase(),
            onCloseClick = onCloseClick
        )

        when (state) {
            SizeSelectorState.Empty -> CartSizePickerLoading()
            else -> {
                DetailsSizeSelector(
                    state = state,
                    onSizeClick = onSizeClick,
                    onSizeTableClick = {}
                )
            }
        }

        SharedButton(
            onClick = onConfirmClick,
            text = stringResource(ClientStrings.CartSelectSizeForPaymentButton),
            textStyle = MaterialTheme.typography.medium15.copy(
                textAlign = TextAlign.Center,
                letterSpacing = .3.sp
            ),
            enabled = state.sizes.any { it.selected },
            disabledContainerColor = MaterialTheme.colorScheme.disabled,
            disabledContentColor = MaterialTheme.colorScheme.onDisabled,
            modifier = Modifier
                .padding(start = 16.dp, top = 28.dp, end = 16.dp, bottom = 8.dp)
                .placeholder(
                    visible = state == SizeSelectorState.Empty,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp)
                )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartSizePickerSheetContentPreview(
    @PreviewParameter(SizeSelectorStateProvider::class) state: SizeSelectorState
) {
    CartSizePickerSheetContent(
        state = state,
        onSizeClick = {},
        onConfirmClick = {},
        onCloseClick = {}
    )
}
