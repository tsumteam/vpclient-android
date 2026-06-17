package ru.mercury.vpclient.shared.ui.components.video

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.icons.Pause33
import ru.mercury.vpclient.shared.ui.icons.Play33
import ru.mercury.vpclient.shared.ui.preview.BooleanPreviewParameterProvider
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.primary40

@Composable
fun VideoPlaybackButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(99.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary40)
    ) {
        Icon(
            imageVector = when {
                isPlaying -> Pause33
                else -> Play33
            },
            contentDescription = null,
            modifier = Modifier.size(width = 33.dp, height = 37.dp),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun VideoPlaybackButtonPreview(
    @PreviewParameter(BooleanPreviewParameterProvider::class) isPlaying: Boolean
) {
    VideoPlaybackButton(
        isPlaying = isPlaying,
        onClick = {}
    )
}
