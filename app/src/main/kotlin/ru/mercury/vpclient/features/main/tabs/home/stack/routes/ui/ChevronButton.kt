package ru.mercury.vpclient.features.main.tabs.home.stack.routes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.theme.VPClientIcons
import ru.mercury.vpclient.core.ui.theme.VPClientTheme

@Composable
fun ChevronButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .background(color = Color.White, shape = CircleShape)
            .size(36.dp)
    ) {
        Icon(
            painter = painterResource(VPClientIcons.ChevronEnd),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview
@Composable
private fun ChevronButtonPreview() {
    VPClientTheme {
        ChevronButton(
            onClick = {}
        )
    }
}
