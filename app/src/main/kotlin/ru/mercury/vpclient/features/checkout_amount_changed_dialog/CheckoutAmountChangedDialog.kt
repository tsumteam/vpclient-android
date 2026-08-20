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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.features.checkout_amount_changed_dialog.intent.CheckoutAmountChangedIntent
import ru.mercury.vpclient.features.checkout_amount_changed_dialog.model.CheckoutAmountChangedModel
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.medium19
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun CheckoutAmountChangedDialog(
    state: CheckoutAmountChangedModel,
    dispatch: (CheckoutAmountChangedIntent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { dispatch(CheckoutAmountChangedIntent.ConfirmClick) },
        confirmButton = {
            TextButton(
                onClick = { dispatch(CheckoutAmountChangedIntent.ConfirmClick) }
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
                text = state.message,
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
private fun CheckoutAmountChangedDialogPreview(
    @PreviewParameter(CheckoutAmountChangedModelProvider::class) state: CheckoutAmountChangedModel
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        CheckoutAmountChangedDialog(
            state = state,
            dispatch = {}
        )
    }
}

private class CheckoutAmountChangedModelProvider: PreviewParameterProvider<CheckoutAmountChangedModel> {
    override val values: Sequence<CheckoutAmountChangedModel> = sequenceOf(
        CheckoutAmountChangedModel(
            message = "Стоимость товаров в заказе изменилась. Пожалуйста, проверьте актуальную сумму заказа."
        )
    )
}
