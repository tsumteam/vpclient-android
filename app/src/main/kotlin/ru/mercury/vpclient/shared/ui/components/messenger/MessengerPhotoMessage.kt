package ru.mercury.vpclient.shared.ui.components.messenger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium22

data class MessengerPhotoMessageState(
    val imageUrls: List<String>,
    val isOutgoing: Boolean
) {
    val visibleImageUrls: List<String>
        get() = imageUrls.take(4)

    val remainingCount: Int
        get() = (imageUrls.size - 4).coerceAtLeast(0)

    val isRemainingBadgeVisible: Boolean
        get() = remainingCount > 0
}

@Composable
fun MessengerPhotoMessage(
    state: MessengerPhotoMessageState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = if (state.isOutgoing) Alignment.End else Alignment.Start
    ) {
        when (state.visibleImageUrls.size) {
            0 -> { }
            1 -> {
                ClientAsyncImage(
                    imageUrl = state.visibleImageUrls[0],
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = 17.dp,
                                topEnd = 17.dp,
                                bottomEnd = if (state.isOutgoing) 0.dp else 17.dp,
                                bottomStart = if (state.isOutgoing) 17.dp else 0.dp
                            )
                        )
                        .background(MaterialTheme.colorScheme.outlineVariant)
                        .size(width = 248.dp, height = 248.dp)
                )
            }
            2 -> {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.outlineVariant),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    ClientAsyncImage(
                        imageUrl = state.visibleImageUrls[0],
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(width = 123.dp, height = 248.dp)
                    )

                    ClientAsyncImage(
                        imageUrl = state.visibleImageUrls[1],
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(width = 123.dp, height = 248.dp)
                    )
                }
            }
            3 -> {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.outlineVariant),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    ClientAsyncImage(
                        imageUrl = state.visibleImageUrls[0],
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(width = 123.dp, height = 248.dp)
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        ClientAsyncImage(
                            imageUrl = state.visibleImageUrls[1],
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(123.dp)
                        )

                        ClientAsyncImage(
                            imageUrl = state.visibleImageUrls[2],
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(123.dp)
                        )
                    }
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.outlineVariant),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        ClientAsyncImage(
                            imageUrl = state.visibleImageUrls[0],
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(123.dp)
                        )

                        ClientAsyncImage(
                            imageUrl = state.visibleImageUrls[1],
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(123.dp)
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        ClientAsyncImage(
                            imageUrl = state.visibleImageUrls[2],
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.size(123.dp)
                        )

                        Box(
                            modifier = Modifier.size(123.dp)
                        ) {
                            ClientAsyncImage(
                                imageUrl = state.visibleImageUrls[3],
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.matchParentSize()
                            )

                            if (state.isRemainingBadgeVisible) {
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .background(MaterialTheme.colorScheme.scrim.copy(alpha = .6F)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "+${state.remainingCount}",
                                        style = MaterialTheme.typography.medium22.copy(
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
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
    private val imageUrls = List(8) { index -> "https://example.com/photo-${index + 1}.jpg" }

    override val values: Sequence<MessengerPhotoMessageState> = sequenceOf(
        MessengerPhotoMessageState(
            imageUrls = imageUrls.take(1),
            isOutgoing = true
        ),
        MessengerPhotoMessageState(
            imageUrls = imageUrls.take(2),
            isOutgoing = false
        ),
        MessengerPhotoMessageState(
            imageUrls = imageUrls.take(3),
            isOutgoing = true
        ),
        MessengerPhotoMessageState(
            imageUrls = imageUrls.take(4),
            isOutgoing = false
        ),
        MessengerPhotoMessageState(
            imageUrls = imageUrls.take(6),
            isOutgoing = true
        )
    )
}
