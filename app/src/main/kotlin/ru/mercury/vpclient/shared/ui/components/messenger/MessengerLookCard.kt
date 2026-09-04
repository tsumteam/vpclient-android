package ru.mercury.vpclient.shared.ui.components.messenger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.MessengerPayloadLook
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.ktx.clickableWithoutRipple
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular12

data class MessengerLookCardState(
    val look: MessengerPayloadLook,
    val onBubbleClick: () -> Unit = {}
) {
    val isCompilationNameVisible: Boolean
        get() = !look.compilationName.isNullOrEmpty()
}

@Composable
fun MessengerLookCard(
    state: MessengerLookCardState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickableWithoutRipple { state.onBubbleClick() }
            .widthIn(max = 300.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(MaterialTheme.colorScheme.outlineVariant)
            .padding(all = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        ClientAsyncImage(
            imageUrl = state.look.imageUrl.orEmpty(),
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(width = 85.dp, height = 130.dp)
        )

        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            if (state.isCompilationNameVisible) {
                Text(
                    text = state.look.compilationName.orEmpty(),
                    style = MaterialTheme.typography.regular12.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 15.sp
                    )
                )
            }

            if (state.look.name.isNotEmpty()) {
                Text(
                    text = state.look.name,
                    style = MaterialTheme.typography.medium15.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 15.sp,
                        letterSpacing = .3.sp
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun MessengerLookCardPreview(
    @PreviewParameter(MessengerLookCardStatePreviewParameterProvider::class) state: MessengerLookCardState
) {
    MessengerLookCard(
        state = state
    )
}

private class MessengerLookCardStatePreviewParameterProvider: PreviewParameterProvider<MessengerLookCardState> {
    override val values: Sequence<MessengerLookCardState> = sequenceOf(
        MessengerLookCardState(
            look = MessengerPayloadLook(
                id = "look-1",
                name = "Образ 1",
                imageUrl = "https://example.com/look-1.jpg",
                compilationName = "BLV/Hotel"
            )
        ),
        MessengerLookCardState(
            look = MessengerPayloadLook(
                id = "look-2",
                name = "Образ 2",
                imageUrl = "https://example.com/look-2.jpg"
            )
        )
    )
}
