package ru.mercury.vpclient.shared.ui.components.messenger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.icons.Play33
import ru.mercury.vpclient.shared.ui.ktx.clickableWithoutRipple
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

data class MessengerVideoMessageState(
    val previewUrl: String,
    val isOutgoing: Boolean,
    val onBubbleClick: () -> Unit = {}
)

@Composable
fun MessengerVideoMessage(
    state: MessengerVideoMessageState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (state.isOutgoing) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .clickableWithoutRipple { state.onBubbleClick() }
                .size(width = 248.dp, height = 248.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 17.dp,
                        topEnd = 17.dp,
                        bottomEnd = if (state.isOutgoing) 0.dp else 17.dp,
                        bottomStart = if (state.isOutgoing) 17.dp else 0.dp
                    )
                )
                .background(MaterialTheme.colorScheme.outlineVariant),
            contentAlignment = Alignment.Center
        ) {
            ClientAsyncImage(
                imageUrl = state.previewUrl,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = .28F))
            )

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = .5F)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Play33,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun MessengerVideoMessagePreview(
    @PreviewParameter(MessengerVideoMessageStatePreviewParameterProvider::class) state: MessengerVideoMessageState
) {
    MessengerVideoMessage(
        state = state
    )
}

private class MessengerVideoMessageStatePreviewParameterProvider: PreviewParameterProvider<MessengerVideoMessageState> {
    override val values: Sequence<MessengerVideoMessageState> = sequenceOf(
        MessengerVideoMessageState(
            previewUrl = "https://example.com/video-preview.jpg",
            isOutgoing = true
        ),
        MessengerVideoMessageState(
            previewUrl = "https://example.com/video-preview.jpg",
            isOutgoing = false
        )
    )
}
