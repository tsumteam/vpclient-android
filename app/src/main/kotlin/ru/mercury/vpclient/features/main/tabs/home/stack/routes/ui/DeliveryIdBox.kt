package ru.mercury.vpclient.features.main.tabs.home.stack.routes.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.theme.VPClientIcons
import ru.mercury.vpclient.core.ui.theme.VPClientTheme
import ru.mercury.vpclient.core.ui.theme.VPClientTypography

@Composable
fun DeliveryIdBox(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(28.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            maxLines = 1,
            style = VPClientTypography.Medium_14_OnBackground
        )

        Icon(
            painter = painterResource(VPClientIcons.Copy),
            contentDescription = null,
            modifier = Modifier.padding(start = 6.dp).size(24.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview
@Preview(fontScale = 1.5F)
@Composable
private fun DeliveryIdBoxPreview() {
    VPClientTheme {
        DeliveryIdBox(
            text = "ЗНД 12345",
            onClick = {},
            modifier = Modifier
                .background(Color.White)
                .padding(8.dp)
        )
    }
}
