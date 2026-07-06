package ru.mercury.vpclient.features.fitting_address_delete_dialog

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
import ru.mercury.vpclient.features.fitting_address_delete_dialog.intent.FittingAddressDeleteDialogIntent
import ru.mercury.vpclient.features.fitting_address_delete_dialog.model.FittingAddressDeleteDialogModel
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.medium19
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun FittingAddressDeleteDialog(
    state: FittingAddressDeleteDialogModel,
    dispatch: (FittingAddressDeleteDialogIntent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { dispatch(FittingAddressDeleteDialogIntent.DismissRequest) },
        confirmButton = {
            TextButton(
                onClick = { dispatch(FittingAddressDeleteDialogIntent.ConfirmClick) }
            ) {
                Text(
                    text = stringResource(ClientStrings.FittingAddressDelete),
                    style = MaterialTheme.typography.medium14.copy(
                        color = MaterialTheme.colorScheme.error
                    )
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { dispatch(FittingAddressDeleteDialogIntent.DismissRequest) }
            ) {
                Text(
                    text = stringResource(ClientStrings.FittingAddressCancel),
                    style = MaterialTheme.typography.medium14.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        },
        title = {
            Text(
                text = stringResource(ClientStrings.FittingAddressDeleteTitle),
                style = MaterialTheme.typography.medium19.copy(
                    lineHeight = 24.sp
                )
            )
        },
        text = {
            Text(
                text = stringResource(ClientStrings.FittingAddressDeleteMessage, state.address),
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
private fun FittingAddressDeleteDialogPreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        FittingAddressDeleteDialog(
            state = FittingAddressDeleteDialogModel(
                address = "Москва, Тверская улица, 1"
            ),
            dispatch = {}
        )
    }
}
