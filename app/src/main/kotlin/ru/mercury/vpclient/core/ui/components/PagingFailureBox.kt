package ru.mercury.vpclient.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.regular15

@Composable
fun PagingFailureBox(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(ClientStrings.CommonRetry),
            style = MaterialTheme.typography.regular15.copy(color = MaterialTheme.colorScheme.onBackground)
        )
    }
}

@FontScalePreviews
@Composable
private fun PagingFailureBoxPreview() {
    ClientTheme {
        PagingFailureBox(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        )
    }
}
