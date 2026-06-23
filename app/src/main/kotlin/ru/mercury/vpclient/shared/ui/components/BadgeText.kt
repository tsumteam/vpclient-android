package ru.mercury.vpclient.shared.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular11

@Composable
fun BadgeText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .height(13.dp)
            .background(
                color = MaterialTheme.colorScheme.error,
                shape = RoundedCornerShape(11.dp)
            )
            .padding(horizontal = 3.dp),
        style = MaterialTheme.typography.regular11.copy(
            color = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun BadgeTextPreview(
    @PreviewParameter(BadgeTextPreviewParameterProvider::class) text: String
) {
    BadgeText(
        text = text
    )
}

private class BadgeTextPreviewParameterProvider: PreviewParameterProvider<String> {
    override val values: Sequence<String> = sequenceOf(
        "1",
        "11",
        "111"
    )
}
