@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.fitting_address_delete_dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.features.fitting_address_delete_dialog.intent.FittingAddressDeleteDialogIntent
import ru.mercury.vpclient.features.fitting_address_delete_dialog.model.FittingAddressDeleteDialogModel
import ru.mercury.vpclient.shared.ui.components.SharedBasicAlertDialog3
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.medium19
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun FittingAddressDeleteDialog(
    state: FittingAddressDeleteDialogModel,
    dispatch: (FittingAddressDeleteDialogIntent) -> Unit
) {
    SharedBasicAlertDialog3(
        onDismissRequest = { dispatch(FittingAddressDeleteDialogIntent.DismissRequest) },
        negativeButton = {
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
        positiveButton = {
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
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(ClientStrings.FittingAddressDeleteTitle),
                style = MaterialTheme.typography.medium19.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 24.sp
                )
            )

            Text(
                text = stringResource(ClientStrings.FittingAddressDeleteMessage, state.address),
                style = MaterialTheme.typography.regular15.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 19.sp,
                    letterSpacing = .2.sp
                )
            )
        }
    }
}
