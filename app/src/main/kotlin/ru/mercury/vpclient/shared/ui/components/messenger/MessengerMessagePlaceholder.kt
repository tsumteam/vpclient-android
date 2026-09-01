package ru.mercury.vpclient.shared.ui.components.messenger

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.MessengerMessageDirection
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

@Composable
fun MessengerMessagePlaceholder(
    direction: MessengerMessageDirection,
    modifier: Modifier = Modifier
) {
    val isOutgoing = direction == MessengerMessageDirection.Outgoing

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (isOutgoing) Alignment.End else Alignment.Start
    ) {
        Spacer(
            modifier = Modifier
                .size(width = if (isOutgoing) 208.dp else 264.dp, height = 52.dp)
                .placeholder(
                    shape = RoundedCornerShape(
                        topStart = if (isOutgoing) 16.dp else 0.dp,
                        topEnd = 16.dp,
                        bottomEnd = if (isOutgoing) 0.dp else 16.dp,
                        bottomStart = 16.dp
                    )
                )
        )

        Spacer(
            modifier = Modifier
                .padding(top = 5.dp)
                .size(width = 44.dp, height = 12.dp)
                .placeholder(shape = RoundedCornerShape(4.dp))
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun MessengerMessagePlaceholderPreview(
    @PreviewParameter(MessengerMessageDirectionPreviewParameterProvider::class) direction: MessengerMessageDirection
) {
    MessengerMessagePlaceholder(
        direction = direction
    )
}

private class MessengerMessageDirectionPreviewParameterProvider: PreviewParameterProvider<MessengerMessageDirection> {
    override val values: Sequence<MessengerMessageDirection> = sequenceOf(
        MessengerMessageDirection.Incoming,
        MessengerMessageDirection.Outgoing
    )
}
