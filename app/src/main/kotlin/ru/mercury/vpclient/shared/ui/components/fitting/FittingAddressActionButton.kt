package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.theme.medium14

@Composable
fun FittingAddressActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    error: Boolean = false
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(58.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.medium14.copy(
                color = when {
                    error -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.onBackground
                },
                lineHeight = 18.sp
            )
        )
    }
}
