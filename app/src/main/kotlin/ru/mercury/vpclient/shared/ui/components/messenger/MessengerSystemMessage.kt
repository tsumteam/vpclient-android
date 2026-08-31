package ru.mercury.vpclient.shared.ui.components.messenger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular15

data class MessengerSystemMessageState(
    val title: String,
    val text: String
)

@Composable
fun MessengerSystemMessage(
    state: MessengerSystemMessageState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .widthIn(max = 328.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(MaterialTheme.colorScheme.outlineVariant)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        if (state.title.isNotEmpty()) {
            Text(
                text = state.title,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.medium15.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 19.sp,
                    letterSpacing = .2.sp,
                    textAlign = TextAlign.Center
                )
            )
        }

        Text(
            text = state.text,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.regular15.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 19.sp,
                letterSpacing = .2.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun MessengerSystemMessagePreview(
    @PreviewParameter(MessengerSystemMessageStatePreviewParameterProvider::class) state: MessengerSystemMessageState
) {
    MessengerSystemMessage(
        state = state
    )
}

private class MessengerSystemMessageStatePreviewParameterProvider: PreviewParameterProvider<MessengerSystemMessageState> {
    override val values: Sequence<MessengerSystemMessageState> = sequenceOf(
        MessengerSystemMessageState(
            title = "Заказ №0000000",
            text = "Заказ оформлен и передан в обработку"
        ),
        MessengerSystemMessageState(
            title = "",
            text = "Заказ №0000000 оформлен"
        )
    )
}
