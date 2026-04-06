package ru.mercury.vpclient.core.ui.components.system

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientTheme

@Composable
fun ClientDragHandle(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(top = 8.dp)
            .size(width = 32.dp, height = 4.dp)
            .background(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = RoundedCornerShape(2.dp)
            )
    )
}

@FontScalePreviews
@Composable
private fun ClientDragHandlePreview() {
    ClientTheme {
        ClientDragHandle(
            modifier = Modifier.padding(16.dp)
        )
    }
}
