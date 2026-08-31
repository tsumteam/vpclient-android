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
import ru.mercury.vpclient.shared.data.entity.MessengerPayloadCompilation
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium15
import ru.mercury.vpclient.shared.ui.theme.regular14

data class MessengerCompilationCardState(
    val compilation: MessengerPayloadCompilation
) {
    val isDescriptionVisible: Boolean
        get() = !compilation.compilationDescription.isNullOrEmpty()
}

@Composable
fun MessengerCompilationCard(
    state: MessengerCompilationCardState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .widthIn(max = 300.dp)
            .clip(RoundedCornerShape(17.dp))
            .background(MaterialTheme.colorScheme.outlineVariant)
            .padding(all = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        ClientAsyncImage(
            imageUrl = state.compilation.imageUrl.orEmpty(),
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(width = 85.dp, height = 130.dp)
        )

        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (state.compilation.compilationName.isNotEmpty()) {
                Text(
                    text = state.compilation.compilationName,
                    style = MaterialTheme.typography.medium15.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 15.sp,
                        letterSpacing = .3.sp
                    )
                )
            }

            if (state.isDescriptionVisible) {
                Text(
                    text = state.compilation.compilationDescription.orEmpty(),
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp,
                        letterSpacing = .2.sp
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun MessengerCompilationCardPreview(
    @PreviewParameter(MessengerCompilationCardStatePreviewParameterProvider::class) state: MessengerCompilationCardState
) {
    MessengerCompilationCard(
        state = state
    )
}

private class MessengerCompilationCardStatePreviewParameterProvider: PreviewParameterProvider<MessengerCompilationCardState> {
    override val values: Sequence<MessengerCompilationCardState> = sequenceOf(
        MessengerCompilationCardState(
            compilation = MessengerPayloadCompilation(
                compilationId = 1,
                compilationName = "BLV/Hotel",
                compilationDescription = "Осенняя подборка городских образов",
                imageUrl = "https://example.com/compilation-1.jpg"
            )
        ),
        MessengerCompilationCardState(
            compilation = MessengerPayloadCompilation(
                compilationId = 2,
                compilationName = "Вечерние образы",
                compilationDescription = null,
                imageUrl = "https://example.com/compilation-2.jpg"
            )
        )
    )
}
