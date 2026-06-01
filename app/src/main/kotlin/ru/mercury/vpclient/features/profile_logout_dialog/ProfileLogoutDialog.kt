@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.profile_logout_dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.features.profile_logout_dialog.intent.ProfileLogoutDialogIntent
import ru.mercury.vpclient.features.profile_logout_dialog.model.ProfileLogoutDialogModel
import ru.mercury.vpclient.shared.ui.components.SharedBasicAlertDialog
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.regular14
import ru.mercury.vpclient.shared.ui.theme.regular22

@Composable
fun ProfileLogoutDialog(
    state: ProfileLogoutDialogModel,
    dispatch: (ProfileLogoutDialogIntent) -> Unit
) {
    state
    SharedBasicAlertDialog(
        onDismissRequest = { dispatch(ProfileLogoutDialogIntent.DismissRequest) }
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(ClientStrings.AppName),
                modifier = Modifier
                    .padding(start = 24.dp, top = 24.dp, end = 24.dp),
                style = MaterialTheme.typography.regular22.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 28.sp
                )
            )

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 24.dp, end = 8.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(
                    onClick = { dispatch(ProfileLogoutDialogIntent.DismissRequest) }
                ) {
                    Text(
                        text = stringResource(ClientStrings.AppName),
                        style = MaterialTheme.typography.regular14
                    )
                }

                TextButton(
                    onClick = { dispatch(ProfileLogoutDialogIntent.ConfirmRequest) }
                ) {
                    Text(
                        text = stringResource(ClientStrings.AppName),
                        style = MaterialTheme.typography.medium14.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }
        }
    }
}
