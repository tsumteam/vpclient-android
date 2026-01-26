@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.main.tabs.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.core.ui.theme.VPClientStrings
import ru.mercury.vpclient.core.ui.theme.VPClientTheme
import ru.mercury.vpclient.core.ui.theme.VPClientTypography
import ru.mercury.vpclient.core.ui.theme.surface4

@Composable
fun ProfileLogoutDialog(
    onConfirmRequest: () -> Unit,
    onDismissRequest: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier.fillMaxWidth().wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface4),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(VPClientStrings.AppName),
                modifier = Modifier
                    .padding(start = 24.dp, top = 24.dp, end = 24.dp),
                style = VPClientTypography.Regular_22_OnBackground.copy(lineHeight = 28.sp)
            )

            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 24.dp, end = 8.dp, bottom = 24.dp)
            ) {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text(
                        text = stringResource(VPClientStrings.AppName),
                        style = VPClientTypography.Regular_14_OnBackground
                    )
                }

                TextButton(
                    onClick = onConfirmRequest,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = stringResource(VPClientStrings.AppName),
                        style = VPClientTypography.Medium_14_OnBackground
                    )
                }
            }
        }
    }
}

@Preview
@Preview(fontScale = 1.5F)
@Composable
private fun ProfileLogoutDialogPreview() {
    VPClientTheme {
        ProfileLogoutDialog(
            onConfirmRequest = {},
            onDismissRequest = {}
        )
    }
}
