package ru.mercury.vpclient.shared.ui.components.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular14
import ru.mercury.vpclient.shared.ui.theme.spanRegular14

data class SearchSuggestionItemState(
    val text: String,
    val query: String,
    val onClick: () -> Unit
)

@Composable
fun SearchSuggestionItem(
    state: SearchSuggestionItemState,
    modifier: Modifier = Modifier
) {
    val matchStartIndex = state.text.indexOf(state.query, ignoreCase = true)
    val annotatedText = buildAnnotatedString {
        append(state.text)
        when {
            matchStartIndex >= 0 -> {
                addStyle(
                    style = MaterialTheme.typography.spanRegular14.copy(
                        fontWeight = FontWeight.W700
                    ),
                    start = matchStartIndex,
                    end = matchStartIndex + state.query.length
                )
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = state.onClick)
    ) {
        Text(
            text = annotatedText,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            style = MaterialTheme.typography.regular14.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 18.sp,
                letterSpacing = .2.sp
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

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
private fun SearchSuggestionItemPreview(
    @PreviewParameter(SearchSuggestionItemStateProvider::class) state: SearchSuggestionItemState
) {
    SearchSuggestionItem(
        state = state
    )
}

private class SearchSuggestionItemStateProvider: PreviewParameterProvider<SearchSuggestionItemState> {
    override val values: Sequence<SearchSuggestionItemState> = sequenceOf(
        SearchSuggestionItemState(
            text = "Жилет из шерсти",
            query = "жилет",
            onClick = {}
        )
    )
}
