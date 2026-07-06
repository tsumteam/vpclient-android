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
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.features.profile_logout_dialog.intent.ProfileLogoutDialogIntent
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.medium19
import ru.mercury.vpclient.shared.ui.theme.regular15

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
                    style = MaterialTheme.typography.medium15.copy(
                        color = MaterialTheme.colorScheme.error,
                        letterSpacing = .3.sp
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
                    style = MaterialTheme.typography.medium15.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = .3.sp
                    )
                )
            }
        },
        title = {
            Text(
                text = stringResource(ClientStrings.ProfileLogoutTitle),
                style = MaterialTheme.typography.medium19.copy(
                    lineHeight = 24.sp
                )
            )
        },
        text = {
            Text(
                text = stringResource(ClientStrings.ProfileLogoutMessage),
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
private fun ProfileLogoutDialogPreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ProfileLogoutDialog(
            dispatch = {}
        )
    }
}
