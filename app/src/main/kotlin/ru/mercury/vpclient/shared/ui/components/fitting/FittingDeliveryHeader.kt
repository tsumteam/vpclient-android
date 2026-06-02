package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.PREFIX_SPACE
import ru.mercury.vpclient.shared.ui.icons.ChevronEnd24
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.regular11
import ru.mercury.vpclient.shared.ui.theme.success

data class FittingDeliveryHeaderState(
    val status: String,
    val date: String,
    val address: String,
    val isDelivered: Boolean
) {
    companion object {
        val Empty = FittingDeliveryHeaderState(
            status = "",
            date = "",
            address = "",
            isDelivered = false
        )
    }
}

@Composable
fun FittingDeliveryHeader(
    header: FittingDeliveryHeaderState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    when {
                        header.isDelivered -> {
                            withStyle(SpanStyle(color = MaterialTheme.colorScheme.success)) {
                                append(stringResource(ClientStrings.FittingDeliveryDelivered))
                                append(PREFIX_SPACE)
                            }
                        }
                        header.status.isNotEmpty() -> {
                            withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurfaceVariant)) {
                                append(header.status)
                                append(PREFIX_SPACE)
                            }
                        }
                    }
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                        append(header.date)
                    }
                },
                style = MaterialTheme.typography.medium14.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            )

            Text(
                text = header.address,
                style = MaterialTheme.typography.regular11.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        Icon(
            imageVector = ChevronEnd24,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun FittingDeliveryHeaderPreview(
    @PreviewParameter(FittingDeliveryHeaderStateProvider::class) state: FittingDeliveryHeaderState
) {
    FittingDeliveryHeader(
        header = state,
        onClick = {}
    )
}

private class FittingDeliveryHeaderStateProvider: PreviewParameterProvider<FittingDeliveryHeaderState> {
    override val values: Sequence<FittingDeliveryHeaderState> = sequenceOf(
        FittingDeliveryHeaderState(
            status = "Доставка",
            date = "13 мая 2026 с 10:00 до 12:00",
            address = "Brioni, Третьяковский проезд",
            isDelivered = false
        ),
        FittingDeliveryHeaderState(
            status = "",
            date = "13 мая 2026 с 10:00 до 12:00",
            address = "Brioni, Третьяковский проезд",
            isDelivered = true
        )
    )
}
