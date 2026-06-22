package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.icons.ChevronEnd24
import ru.mercury.vpclient.shared.ui.icons.Selected24
import ru.mercury.vpclient.shared.ui.icons.Unselected24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium14

data class FittingConfirmationPlaceRowState(
    val text: String,
    val selected: Boolean,
    val enabled: Boolean,
    val showChevron: Boolean
)

@Composable
fun FittingConfirmationPlaceRow(
    state: FittingConfirmationPlaceRowState,
    onClick: () -> Unit,
    onChevronClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(enabled = state.enabled, onClick = onClick)
            .padding(start = 16.dp, end = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (state.selected) Selected24 else Unselected24,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = when {
                state.enabled -> MaterialTheme.colorScheme.onBackground
                else -> MaterialTheme.colorScheme.outline
            }
        )

        Text(
            text = state.text,
            modifier = Modifier.weight(1F),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.medium14.copy(
                color = when {
                    state.enabled -> MaterialTheme.colorScheme.onBackground
                    else -> MaterialTheme.colorScheme.outline
                },
                lineHeight = 16.sp
            )
        )

        if (state.showChevron) {
            IconButton(
                onClick = onChevronClick,
                enabled = state.enabled
            ) {
                Icon(
                    imageVector = ChevronEnd24,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = when {
                        state.enabled -> MaterialTheme.colorScheme.onSurfaceVariant
                        else -> MaterialTheme.colorScheme.outline
                    }
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun FittingConfirmationPlaceRowPreview(
    @PreviewParameter(FittingConfirmationPlaceRowStateProvider::class) state: FittingConfirmationPlaceRowState
) {
    FittingConfirmationPlaceRow(
        state = state,
        onClick = {},
        onChevronClick = {}
    )
}

private class FittingConfirmationPlaceRowStateProvider: PreviewParameterProvider<FittingConfirmationPlaceRowState> {
    override val values: Sequence<FittingConfirmationPlaceRowState> = sequenceOf(
        FittingConfirmationPlaceRowState(
            text = "ПВЗ ЦУМ, ул. Петровка, 2",
            selected = false,
            enabled = true,
            showChevron = false
        ),
        FittingConfirmationPlaceRowState(
            text = "ПВЗ ЦУМ, ул. Петровка, 2",
            selected = true,
            enabled = true,
            showChevron = false
        ),
        FittingConfirmationPlaceRowState(
            text = "Москва, Кутузовский проспект, 48",
            selected = false,
            enabled = true,
            showChevron = true
        ),
        FittingConfirmationPlaceRowState(
            text = "Москва, Кутузовский проспект, 48",
            selected = true,
            enabled = true,
            showChevron = true
        ),
        FittingConfirmationPlaceRowState(
            text = "Выберите адрес",
            selected = false,
            enabled = false,
            showChevron = true
        )
    )
}
