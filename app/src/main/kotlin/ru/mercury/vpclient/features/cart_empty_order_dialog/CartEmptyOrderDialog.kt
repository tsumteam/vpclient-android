package ru.mercury.vpclient.features.cart_empty_order_dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.features.cart_empty_order_dialog.intent.CartEmptyOrderDialogIntent
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.medium19
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun CartEmptyOrderDialog(
    dispatch: (CartEmptyOrderDialogIntent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { dispatch(CartEmptyOrderDialogIntent.DismissRequest) },
        confirmButton = {
            TextButton(
                onClick = { dispatch(CartEmptyOrderDialogIntent.DismissRequest) }
            ) {
                Text(
                    text = stringResource(ClientStrings.CartEmptyOrderButton),
                    style = MaterialTheme.typography.medium15.copy(
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = .3.sp
                    )
                )
            }
        },
        title = {
            Text(
                text = stringResource(ClientStrings.CartEmptyOrderTitle),
                style = MaterialTheme.typography.medium19.copy(
                    lineHeight = 24.sp
                )
            )
        },
        text = {
            Text(
                text = stringResource(ClientStrings.CartEmptyOrderMessage),
                style = MaterialTheme.typography.regular15.copy(
                    lineHeight = 19.sp,
                    letterSpacing = .2.sp
                )
            )
        },
        titleContentColor = MaterialTheme.colorScheme.onBackground,
        textContentColor = MaterialTheme.colorScheme.onBackground
    )
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CartEmptyOrderDialogPreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CartEmptyOrderDialog(
            dispatch = {}
        )
    }
}
