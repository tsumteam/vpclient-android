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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.mercury.vpclient.features.cart_fitting_edit_product_sheet.intent.CartFittingEditProductSheetIntent
import ru.mercury.vpclient.shared.ui.components.SharedLazyColumn
import ru.mercury.vpclient.shared.ui.components.SharedModalBottomSheet
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium15

@Composable
fun CartFittingEditProductSheet(
    isSizeSelectionAvailable: Boolean,
    dispatch: (CartFittingEditProductSheetIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    val sheetDispatch: (CartFittingEditProductSheetIntent) -> Unit = { intent ->
        scope.launch {
            sheetState.hide()
            dispatch(intent)
        }
    }

    SharedModalBottomSheet(
        onDismissRequest = { dispatch(CartFittingEditProductSheetIntent.DismissRequest) },
        sheetState = sheetState,
        containerColor = Color.Transparent
    ) {
        SharedLazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 8.dp)
        ) {
            item {
                Button(
                    onClick = { sheetDispatch(CartFittingEditProductSheetIntent.ChangeColorClick) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = when {
                        isSizeSelectionAvailable -> RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
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
            }
            if (isSizeSelectionAvailable) {
                item {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant
                    )
                }
                item {
                    Button(
                        onClick = { sheetDispatch(CartFittingEditProductSheetIntent.ChangeSizeClick) },
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
            }
            item {
                Spacer(
                    modifier = Modifier.height(18.dp)
                )
            }
            item {
                Button(
                    onClick = { sheetDispatch(CartFittingEditProductSheetIntent.ConfirmClick) },
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
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CartFittingEditProductSheetPreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CartFittingEditProductSheet(
            isSizeSelectionAvailable = true,
            dispatch = {}
        )
    }
}
