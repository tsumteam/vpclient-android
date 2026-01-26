package ru.mercury.vpclient.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun VPClientSnackbarHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    containerColor: Color = SnackbarDefaults.color
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
        snackbar = { snackbarData ->
            Snackbar(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable(onClick = snackbarData::dismiss),
                containerColor = containerColor
            ) {
                Text(
                    text = snackbarData.visuals.message,
                    maxLines = 10,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    )
}
