package ru.mercury.vpclient.shared.ui.components.messenger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import ru.mercury.vpclient.shared.data.entity.MessengerMessageStatus
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

data class MessengerPhotoMessageState(
    val imageUrls: List<String>,
    val isOutgoing: Boolean,
    val metaState: MessengerMessageMetaState
)

@Composable
fun MessengerPhotoMessage(
    state: MessengerPhotoMessageState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = if (state.isOutgoing) Alignment.End else Alignment.Start
    ) {
        state.imageUrls.forEach { imageUrl ->
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                horizontalAlignment = if (state.isOutgoing) Alignment.End else Alignment.Start
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 248.dp, height = 248.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 17.dp,
                                topEnd = 17.dp,
                                bottomEnd = if (state.isOutgoing) 0.dp else 17.dp,
                                bottomStart = if (state.isOutgoing) 17.dp else 0.dp
                            )
                        )
                        .background(MaterialTheme.colorScheme.outlineVariant)
                ) {
                    ClientAsyncImage(
                        imageUrl = imageUrl,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )
                }

                MessengerMessageMeta(
                    state = state.metaState
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun MessengerPhotoMessagePreview(
    @PreviewParameter(MessengerPhotoMessageStatePreviewParameterProvider::class) state: MessengerPhotoMessageState
) {
    MessengerPhotoMessage(
        state = state
    )
}

private class MessengerPhotoMessageStatePreviewParameterProvider: PreviewParameterProvider<MessengerPhotoMessageState> {
    private val imageUrls = List(4) { index -> "https://example.com/photo-${index + 1}.jpg" }

    private val outgoingMetaState = MessengerMessageMetaState(
        createTime = "2026-08-26T16:40:00+03:00",
        isEdited = false,
        status = MessengerMessageStatus.Read
    )

    private val incomingMetaState = MessengerMessageMetaState(
        createTime = "2026-08-26T16:40:00+03:00",
        isEdited = false,
        status = null
    )

    override val values: Sequence<MessengerPhotoMessageState> = sequenceOf(
        MessengerPhotoMessageState(
            imageUrls = imageUrls.take(1),
            isOutgoing = true,
            metaState = outgoingMetaState
        ),
        MessengerPhotoMessageState(
            imageUrls = imageUrls.take(3),
            isOutgoing = false,
            metaState = incomingMetaState
        ),
        MessengerPhotoMessageState(
            imageUrls = imageUrls.take(3),
            isOutgoing = true,
            metaState = outgoingMetaState
        )
    )
}
