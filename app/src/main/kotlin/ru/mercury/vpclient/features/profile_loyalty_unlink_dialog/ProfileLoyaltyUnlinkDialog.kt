package ru.mercury.vpclient.features.profile_loyalty_unlink_dialog

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
import ru.mercury.vpclient.features.profile_loyalty_unlink_dialog.intent.ProfileLoyaltyUnlinkDialogIntent
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.medium19
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun ProfileLoyaltyUnlinkDialog(
    dispatch: (ProfileLoyaltyUnlinkDialogIntent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { dispatch(ProfileLoyaltyUnlinkDialogIntent.DismissRequest) },
        confirmButton = {
            TextButton(
                onClick = { dispatch(ProfileLoyaltyUnlinkDialogIntent.ConfirmRequest) }
            ) {
                Text(
                    text = stringResource(ClientStrings.ProfileLoyaltyUnlinkConfirm),
                    style = MaterialTheme.typography.medium15.copy(
                        color = MaterialTheme.colorScheme.error,
                        letterSpacing = .3.sp
                    )
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { dispatch(ProfileLoyaltyUnlinkDialogIntent.DismissRequest) }
            ) {
                Text(
                    text = stringResource(ClientStrings.ProfileLoyaltyUnlinkCancel),
                    style = MaterialTheme.typography.medium15.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = .3.sp
                    )
                )
            }
        },
        title = {
            Text(
                text = stringResource(ClientStrings.ProfileLoyaltyUnlinkTitle),
                style = MaterialTheme.typography.medium19.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 24.sp
                )
            )
        },
        text = {
            Text(
                text = stringResource(ClientStrings.ProfileLoyaltyUnlinkMessage),
                style = MaterialTheme.typography.regular15.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 19.sp,
                    letterSpacing = .2.sp
                )
            )
        }
    )
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun ProfileLoyaltyUnlinkDialogPreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ProfileLoyaltyUnlinkDialog(
            dispatch = {}
        )
    }
}
