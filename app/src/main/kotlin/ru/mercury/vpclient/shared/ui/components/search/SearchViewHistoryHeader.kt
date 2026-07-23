package ru.mercury.vpclient.shared.ui.components.search

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.livretMedium15

data class SearchViewHistoryHeaderState(
    val title: String
)

@Composable
fun SearchViewHistoryHeader(
    state: SearchViewHistoryHeaderState,
    modifier: Modifier = Modifier
) {
    Text(
        text = state.title,
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 16.dp)
            .wrapContentHeight(align = Alignment.CenterVertically),
        style = MaterialTheme.typography.livretMedium15.copy(
            color = MaterialTheme.colorScheme.onBackground
        )
    )
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun SearchViewHistoryHeaderPreview(
    @PreviewParameter(SearchViewHistoryHeaderStateProvider::class) state: SearchViewHistoryHeaderState
) {
    SearchViewHistoryHeader(
        state = state
    )
}

private class SearchViewHistoryHeaderStateProvider: PreviewParameterProvider<SearchViewHistoryHeaderState> {
    override val values: Sequence<SearchViewHistoryHeaderState> = sequenceOf(
        SearchViewHistoryHeaderState(
            title = "ВЫ НЕДАВНО СМОТРЕЛИ"
        )
    )
}
