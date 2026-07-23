package ru.mercury.vpclient.shared.ui.components.search

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.livretMedium15
import ru.mercury.vpclient.shared.ui.theme.medium15

data class SearchHistoryHeaderState(
    val title: String,
    val clearButtonText: String,
    val onClearClick: () -> Unit
)

@Composable
fun SearchHistoryHeader(
    state: SearchHistoryHeaderState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 16.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = state.title,
            modifier = Modifier.weight(1F),
            style = MaterialTheme.typography.livretMedium15.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )

        TextButton(
            onClick = state.onClearClick,
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            ),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            Text(
                text = state.clearButtonText,
                style = MaterialTheme.typography.medium15.copy(
                    lineHeight = 15.sp,
                    letterSpacing = .3.sp
                )
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun SearchHistoryHeaderPreview(
    @PreviewParameter(SearchHistoryHeaderStateProvider::class) state: SearchHistoryHeaderState
) {
    SearchHistoryHeader(
        state = state
    )
}

private class SearchHistoryHeaderStateProvider: PreviewParameterProvider<SearchHistoryHeaderState> {
    override val values: Sequence<SearchHistoryHeaderState> = sequenceOf(
        SearchHistoryHeaderState(
            title = "ВЫ НЕДАВНО ИСКАЛИ",
            clearButtonText = "Очистить",
            onClearClick = {}
        )
    )
}
