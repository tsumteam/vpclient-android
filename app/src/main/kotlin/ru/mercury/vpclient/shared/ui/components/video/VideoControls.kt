package ru.mercury.vpclient.shared.ui.components.video

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.regular14
import java.util.Locale

data class VideoControlsState(
    private val positionMs: Long,
    private val durationMs: Long
) {
    val sliderDurationMs: Long
        get() = durationMs.coerceAtLeast(1L)

    val sliderPositionMs: Long
        get() = positionMs.coerceIn(0L, sliderDurationMs)

    val positionText: String
        get() = positionMs.videoPositionText()

    val durationText: String
        get() = durationMs.videoDurationText()

    private fun Long.videoPositionText(): String {
        val totalSeconds = (this / MILLIS_IN_SECOND).coerceAtLeast(0L)
        val minutes = totalSeconds / SECONDS_IN_MINUTE
        val seconds = totalSeconds % SECONDS_IN_MINUTE
        return String.format(Locale.getDefault(), "%d:%02d", minutes, seconds)
    }

    private fun Long.videoDurationText(): String {
        val totalSeconds = (this / MILLIS_IN_SECOND).coerceAtLeast(0L)
        val minutes = totalSeconds / SECONDS_IN_MINUTE
        val seconds = totalSeconds % SECONDS_IN_MINUTE
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    private companion object {
        private const val MILLIS_IN_SECOND = 1000L
        private const val SECONDS_IN_MINUTE = 60L
    }
}

@Composable
fun VideoControls(
    state: VideoControlsState,
    onSeek: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(MaterialTheme.colorScheme.primary)
            .navigationBarsPadding()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        VideoProgressSlider(
            modifier = Modifier.weight(1F),
            positionMs = state.sliderPositionMs,
            durationMs = state.sliderDurationMs,
            onSeek = onSeek
        )

        Text(
            text = stringResource(
                ClientStrings.VideoTime,
                state.positionText,
                state.durationText
            ),
            style = MaterialTheme.typography.regular14.copy(
                color = MaterialTheme.colorScheme.onPrimary,
                lineHeight = 18.sp,
                letterSpacing = .2.sp
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun VideoControlsPreview() {
    VideoControls(
        state = VideoControlsState(
            positionMs = 6_000L,
            durationMs = 15_000L
        ),
        onSeek = {}
    )
}
