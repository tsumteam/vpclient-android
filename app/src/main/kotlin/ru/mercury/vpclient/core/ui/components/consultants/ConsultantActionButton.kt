package ru.mercury.vpclient.core.ui.components.consultants

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.components.IndicatorIcon
import ru.mercury.vpclient.core.ui.icons.Phone24
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.regular11
import ru.mercury.vpclient.core.ui.theme.surface3

@Composable
fun ConsultantActionButton(
    icon: ImageVector,
    label: String,
    badge: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .defaultMinSize(minWidth = 1.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surface3)
            .clickable(onClick = onClick)
            .padding(start = 4.dp, top = 5.dp, end = 4.dp, bottom = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.Top)
    ) {
        IndicatorIcon(
            icon = icon,
            showIndicator = badge
        )

        Text(
            text = label,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            style = MaterialTheme.typography.regular11.copy(
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        )
    }
}

@FontScalePreviews
@Composable
private fun ConsultantActionButtonPreview() {
    ClientTheme {
        ConsultantActionButton(
            icon = Phone24,
            label = "Позвонить",
            badge = true,
            onClick = {},
            modifier = Modifier
                .padding(16.dp)
                .width(69.dp)
        )
    }
}
