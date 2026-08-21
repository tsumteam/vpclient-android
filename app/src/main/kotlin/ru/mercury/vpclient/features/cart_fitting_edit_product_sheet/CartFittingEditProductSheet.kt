@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.cart_fitting_edit_product_sheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.features.cart_fitting_edit_product_sheet.intent.CartFittingEditProductIntent
import ru.mercury.vpclient.features.cart_fitting_edit_product_sheet.model.CartFittingEditProductModel
import ru.mercury.vpclient.shared.ui.components.SharedColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium15

@Composable
fun CartFittingEditProductSheet(
    state: CartFittingEditProductModel,
    dispatch: (CartFittingEditProductIntent) -> Unit
) {
    SharedModalBottomSheet(
        onDismissRequest = { dispatch(CartFittingEditProductIntent.DismissClick) },
        containerColor = Color.Transparent
    ) {
        SharedColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 8.dp)
        ) {
            Button(
                onClick = { dispatch(CartFittingEditProductIntent.ChangeColorClick) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = when {
                    state.isSizeSelectionAvailable -> RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                    else -> RoundedCornerShape(8.dp)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                )
            ) {
                Text(
                    text = stringResource(ClientStrings.CartEditChangeColor),
                    style = MaterialTheme.typography.medium15
                )
            }

            if (state.isSizeSelectionAvailable) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                Button(
                    onClick = { dispatch(CartFittingEditProductIntent.ChangeSizeClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                ) {
                    Text(
                        text = stringResource(ClientStrings.CartEditSelectSize),
                        style = MaterialTheme.typography.medium15
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Button(
                onClick = { dispatch(CartFittingEditProductIntent.DismissClick) },
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
                    style = MaterialTheme.typography.medium15
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CartFittingEditProductSheetPreview(
    @PreviewParameter(CartFittingEditProductModelPreviewParameterProvider::class) state: CartFittingEditProductModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CartFittingEditProductSheet(
            state = state,
            dispatch = {}
        )
    }
}

private class CartFittingEditProductModelPreviewParameterProvider: PreviewParameterProvider<CartFittingEditProductModel> {
    override val values: Sequence<CartFittingEditProductModel> = sequenceOf(
        CartFittingEditProductModel(isSizeSelectionAvailable = true),
        CartFittingEditProductModel(isSizeSelectionAvailable = false)
    )
}
