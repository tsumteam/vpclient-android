package ru.mercury.vpclient.features.checkout_amount_changed_dialog

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
import ru.mercury.vpclient.features.checkout_amount_changed_dialog.intent.CheckoutAmountChangedDialogIntent
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.medium19
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun CheckoutAmountChangedDialog(
    message: String,
    dispatch: (CheckoutAmountChangedDialogIntent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { dispatch(CheckoutAmountChangedDialogIntent.ContinueClick) },
        confirmButton = {
            TextButton(
                onClick = { dispatch(CheckoutAmountChangedDialogIntent.ContinueClick) }
            ) {
                Text(
                    text = stringResource(ClientStrings.CheckoutAmountChangedButton),
                    style = MaterialTheme.typography.medium15.copy(
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = .3.sp
                    )
                )
            }
        },
        title = {
            Text(
                text = stringResource(ClientStrings.CheckoutAmountChangedTitle),
                style = MaterialTheme.typography.medium19.copy(
                    lineHeight = 24.sp
                )
            )
        },
        text = {
            Text(
                text = message,
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
private fun CheckoutAmountChangedDialogPreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CheckoutAmountChangedDialog(
            message = "Стоимость товаров в заказе изменилась. Пожалуйста, проверьте актуальную сумму заказа.",
            dispatch = {}
        )
    }
}
