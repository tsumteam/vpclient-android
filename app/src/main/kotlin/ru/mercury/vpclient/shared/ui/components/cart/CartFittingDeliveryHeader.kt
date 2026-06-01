package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.FittingDeliveryHeader
import ru.mercury.vpclient.shared.ui.icons.ChevronStart24
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.regular11
import ru.mercury.vpclient.shared.ui.theme.success

@Composable
fun CartFittingDeliveryHeader(
    header: FittingDeliveryHeader,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val primaryTextColor = MaterialTheme.colorScheme.onBackground
    val secondaryTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    val statusTextColor = when {
        header.isDelivered -> MaterialTheme.colorScheme.success
        else -> secondaryTextColor
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(start = 16.dp, top = 7.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    if (header.status.isNotEmpty()) {
                        withStyle(SpanStyle(color = statusTextColor)) {
                            append(header.status)
                            append(" ")
                        }
                    }
                    withStyle(SpanStyle(color = primaryTextColor)) {
                        append(header.date)
                    }
                },
                style = MaterialTheme.typography.medium14.copy(
                    color = secondaryTextColor,
                    lineHeight = 16.sp
                )
            )

            Text(
                text = header.address,
                style = MaterialTheme.typography.regular11.copy(
                    color = secondaryTextColor
                )
            )
        }

        Icon(
            imageVector = ChevronStart24,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
                .size(24.dp)
                .graphicsLayer { scaleX = -1F },
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartFittingDeliveryHeaderPreview() {
    CartFittingDeliveryHeader(
        header = FittingDeliveryHeader(
            status = "Доставка",
            date = "13 мая 2026 с 10:00 до 12:00",
            address = "Brioni, Третьяковский проезд",
            isDelivered = false
        ),
        onClick = {}
    )
}
