@file:OptIn(ExperimentalMaterial3Api::class)

package ru.mercury.vpclient.features.debug_env_dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.features.debug_env_dialog.intent.DebugEnvironmentDialogIntent
import ru.mercury.vpclient.features.debug_env_dialog.model.DebugEnvironmentDialogModel
import ru.mercury.vpclient.shared.data.network.env.ClientEnvironment
import ru.mercury.vpclient.shared.ui.components.RadioRow
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular22

@Composable
fun DebugEnvironmentDialog(
    state: DebugEnvironmentDialogModel,
    dispatch: (DebugEnvironmentDialogIntent) -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = { dispatch(DebugEnvironmentDialogIntent.DismissRequest) },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
    ) {
        Column {
            Text(
                text = "Выберите окружение",
                modifier = Modifier.padding(start = 24.dp, top = 24.dp, end = 24.dp),
                style = MaterialTheme.typography.regular22.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 22.sp
                )
            )

            RadioRow(
                text = ClientEnvironment.TEST.name,
                selected = state.selectedEnvironment == ClientEnvironment.TEST,
                onSelect = {
                    dispatch(DebugEnvironmentDialogIntent.SelectEnvironment(ClientEnvironment.TEST))
                    dispatch(DebugEnvironmentDialogIntent.DismissRequest)
                },
                modifier = Modifier.padding(start = 8.dp, top = 24.dp, end = 8.dp)
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 24.dp),
                color = MaterialTheme.colorScheme.outline
            )

            RadioRow(
                text = ClientEnvironment.UAT.name,
                selected = state.selectedEnvironment == ClientEnvironment.UAT,
                onSelect = {
                    dispatch(DebugEnvironmentDialogIntent.SelectEnvironment(ClientEnvironment.UAT))
                    dispatch(DebugEnvironmentDialogIntent.DismissRequest)
                },
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 24.dp),
                color = MaterialTheme.colorScheme.outline
            )

            RadioRow(
                text = ClientEnvironment.PROD.name,
                selected = state.selectedEnvironment == ClientEnvironment.PROD,
                onSelect = {
                    dispatch(DebugEnvironmentDialogIntent.SelectEnvironment(ClientEnvironment.PROD))
                    dispatch(DebugEnvironmentDialogIntent.DismissRequest)
                },
                modifier = Modifier.padding(horizontal = 8.dp).padding(bottom = 16.dp)
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun DebugEnvironmentDialogPreview() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        DebugEnvironmentDialog(
            state = DebugEnvironmentDialogModel(
                selectedEnvironment = ClientEnvironment.TEST
            ),
            dispatch = {}
        )
    }
}
