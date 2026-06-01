package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.theme.medium14

@Composable
fun FittingConfirmationPlaceRow(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    showChevron: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            enabled = enabled
        )

        Text(
            text = text,
            modifier = Modifier.weight(1F),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.medium14.copy(
                color = when {
                    enabled -> MaterialTheme.colorScheme.onBackground
                    else -> MaterialTheme.colorScheme.outline
                },
                lineHeight = 16.sp
            )
        )

        if (showChevron) {
            Icon(
                imageVector = ChevronStart24,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer { scaleX = -1F },
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
