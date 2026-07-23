package ru.mercury.vpclient.shared.ui.components.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.icons.Cancel14
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium14

data class SearchHistoryItemState(
    val text: String,
    val onClick: () -> Unit,
    val onClearClick: () -> Unit
)

@Composable
fun SearchHistoryItem(
    state: SearchHistoryItemState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable(onClick = state.onClick)
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = state.text,
                modifier = Modifier.weight(1F),
                style = MaterialTheme.typography.medium14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 16.sp
                )
            )

            IconButton(
                onClick = state.onClearClick
            ) {
                Icon(
                    imageVector = Cancel14,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        HorizontalDivider(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun SearchHistoryItemPreview(
    @PreviewParameter(SearchHistoryItemStateProvider::class) state: SearchHistoryItemState
) {
    SearchHistoryItem(
        state = state
    )
}

private class SearchHistoryItemStateProvider: PreviewParameterProvider<SearchHistoryItemState> {
    override val values: Sequence<SearchHistoryItemState> = sequenceOf(
        SearchHistoryItemState(
            text = "жилет",
            onClick = {},
            onClearClick = {}
        )
    )
}
