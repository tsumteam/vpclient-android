package ru.mercury.vpclient.core.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mercury.vpclient.core.ui.icons.Close24
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientTheme

@Composable
fun CloseIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Close24,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@FontScalePreviews
@Composable
private fun CloseIconButtonPreview() {
    ClientTheme {
        CloseIconButton(
            onClick = {}
        )
    }
}
