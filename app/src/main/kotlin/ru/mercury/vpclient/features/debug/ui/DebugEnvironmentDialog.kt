@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.debug.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.core.network.env.VPClientEnvironment
import ru.mercury.vpclient.core.ui.components.RadioRow
import ru.mercury.vpclient.core.ui.theme.VPClientTheme
import ru.mercury.vpclient.core.ui.theme.VPClientTypography
import ru.mercury.vpclient.core.ui.theme.divider2
import ru.mercury.vpclient.core.ui.theme.surface4

@Composable
fun DebugEnvironmentDialog(
    onDismissRequest: () -> Unit,
    selectedEnvironment: VPClientEnvironment,
    onSelectEnvironment: (VPClientEnvironment) -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface4)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Выберите окружение",
                modifier = Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp),
                style = VPClientTypography.Regular_22_OnBackground.copy(lineHeight = 22.sp)
            )

            RadioRow(
                text = VPClientEnvironment.TEST.name,
                selected = selectedEnvironment == VPClientEnvironment.TEST,
                onSelect = {
                    onSelectEnvironment(VPClientEnvironment.TEST)
                    onDismissRequest()
                },
                modifier = Modifier.padding(start = 8.dp, top = 24.dp, end = 8.dp)
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 24.dp),
                color = MaterialTheme.colorScheme.divider2
            )

            RadioRow(
                text = VPClientEnvironment.UAT.name,
                selected = selectedEnvironment == VPClientEnvironment.UAT,
                onSelect = {
                    onSelectEnvironment(VPClientEnvironment.UAT)
                    onDismissRequest()
                },
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 24.dp),
                color = MaterialTheme.colorScheme.divider2
            )

            RadioRow(
                text = VPClientEnvironment.PROD.name,
                selected = selectedEnvironment == VPClientEnvironment.PROD,
                onSelect = {
                    onSelectEnvironment(VPClientEnvironment.PROD)
                    onDismissRequest()
                },
                modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 16.dp)
            )
        }
    }
}

@Preview
@Preview(fontScale = 1.5F)
@Composable
private fun DebugEnvironmentDialogPreview() {
    VPClientTheme {
        DebugEnvironmentDialog(
            onDismissRequest = {},
            selectedEnvironment = VPClientEnvironment.TEST,
            onSelectEnvironment = {}
        )
    }
}
