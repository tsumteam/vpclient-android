package ru.mercury.vpclient.shared.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun PagingFailureBox(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(ClientStrings.CommonRetry),
        modifier = modifier
            .clickable(onClick = onClick)
            .wrapContentSize(Alignment.Center),
        style = MaterialTheme.typography.regular15.copy(
            color = MaterialTheme.colorScheme.onBackground
        )
    )
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
