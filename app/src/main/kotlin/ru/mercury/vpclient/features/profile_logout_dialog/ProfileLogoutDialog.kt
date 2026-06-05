@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.profile_logout_dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import ru.mercury.vpclient.features.profile_logout_dialog.intent.ProfileLogoutDialogIntent
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium14

@Composable
fun ProfileLogoutDialog(
    dispatch: (ProfileLogoutDialogIntent) -> Unit
) {
    AlertDialog(
        onDismissRequest = { dispatch(ProfileLogoutDialogIntent.DismissRequest) },
        confirmButton = {
            TextButton(
                onClick = { dispatch(ProfileLogoutDialogIntent.ConfirmRequest) }
            ) {
                Text(
                    text = stringResource(ClientStrings.ProfileLogout),
                    style = MaterialTheme.typography.medium14.copy(
                        color = MaterialTheme.colorScheme.error
                    )
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { dispatch(ProfileLogoutDialogIntent.DismissRequest) }
            ) {
                Text(
                    text = stringResource(ClientStrings.ProfileLogoutCancel),
                    style = MaterialTheme.typography.medium14.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
            }
        },
        title = {
            Text(
                text = stringResource(ClientStrings.ProfileLogoutTitle),
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        text = {
            Text(
                text = stringResource(ClientStrings.ProfileLogoutMessage),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    )
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun ProfileLogoutDialogPreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ProfileLogoutDialog(
            dispatch = {}
        )
    }
}
