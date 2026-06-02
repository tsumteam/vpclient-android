@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.cart_edit_product_sheet

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.features.cart_edit_product_sheet.intent.CartEditProductSheetIntent
import ru.mercury.vpclient.features.cart_edit_product_sheet.model.CartEditProductSheetModel
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium15

@Composable
fun CartEditProductSheet(
    state: CartEditProductSheetModel,
    dispatch: (CartEditProductSheetIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(CartEditProductSheetIntent.DismissRequest) },
        sheetState = sheetState,
        containerColor = Color.Transparent
    ) {
        CartEditProductSheetContent(
            state = state,
            dispatch = dispatch
        )
    }
}

@Composable
fun CartEditProductSheetContent(
    state: CartEditProductSheetModel,
    dispatch: (CartEditProductSheetIntent) -> Unit
) {
    SharedLazyColumn(
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = 8.dp
        )
    ) {
        state.actions.forEachIndexed { index, action ->
            item {
                Button(
                    onClick = { dispatch(CartEditProductSheetIntent.ActionClick(index)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = when {
                        state.actions.size == 1 -> RoundedCornerShape(8.dp)
                        index == 0 -> RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                        index == state.actions.lastIndex -> RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                        else -> RoundedCornerShape(0.dp)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text(
                        text = action,
                        style = MaterialTheme.typography.medium15.copy(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    )
                }
            }
            if (index < state.actions.lastIndex) {
                item {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
            }
        }
        item {
            Spacer(
                modifier = Modifier.height(18.dp)
            )
        }
        item {
            Button(
                onClick = { dispatch(CartEditProductSheetIntent.DismissRequest) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(
                    text = stringResource(ClientStrings.CartEditCancel),
                    style = MaterialTheme.typography.medium15.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CartEditProductSheetContentPreview(
    @PreviewParameter(CartEditProductSheetModelProvider::class) state: CartEditProductSheetModel
) {
    CartEditProductSheetContent(
        state = state,
        dispatch = {}
    )
}

private class CartEditProductSheetModelProvider: PreviewParameterProvider<CartEditProductSheetModel> {

    override val values: Sequence<CartEditProductSheetModel> = sequenceOf(
        CartEditProductSheetModel(
            actions = listOf(
                "Добавить размер",
                "Изменить размер",
                "Изменить количество",
                "Изменить цвет"
            )
        )
    )
}
