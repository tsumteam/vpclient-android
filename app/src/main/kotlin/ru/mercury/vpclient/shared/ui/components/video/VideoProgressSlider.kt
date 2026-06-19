package ru.mercury.vpclient.shared.ui.components.video

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.secondary2
import kotlin.math.roundToLong

@Composable
fun VideoProgressSlider(
    modifier: Modifier = Modifier,
    positionMs: Long,
    durationMs: Long,
    onSeek: (Long) -> Unit
) {
    val activeColor = MaterialTheme.colorScheme.onPrimary
    val inactiveColor = MaterialTheme.colorScheme.secondary2
    val progress = positionMs.toFloat() / durationMs.toFloat()
    val trackHeight = 4.dp
    val thumbRadius = 6.dp

    Canvas(
        modifier = modifier
            .height(24.dp)
            .pointerInput(durationMs) {
                detectTapGestures { offset ->
                    val nextProgress = (offset.x / size.width).coerceIn(0F, 1F)
                    onSeek((durationMs * nextProgress).roundToLong())
                }
            }
            .pointerInput(durationMs) {
                detectDragGestures { change, _ ->
                    change.consume()
                    val nextProgress = (change.position.x / size.width).coerceIn(0F, 1F)
                    onSeek((durationMs * nextProgress).roundToLong())
                }
            }
    ) {
        val centerY = size.height / 2F
        val trackHeightPx = trackHeight.toPx()
        val thumbRadiusPx = thumbRadius.toPx()
        val activeWidth = size.width * progress.coerceIn(0F, 1F)

        drawRoundRect(
            color = inactiveColor,
            topLeft = Offset(x = 0F, y = centerY - trackHeightPx / 2F),
            size = Size(width = size.width, height = trackHeightPx),
            cornerRadius = CornerRadius(x = trackHeightPx / 2F, y = trackHeightPx / 2F)
        )

        drawRoundRect(
            color = activeColor,
            topLeft = Offset(x = 0F, y = centerY - trackHeightPx / 2F),
            size = Size(width = activeWidth, height = trackHeightPx),
            cornerRadius = CornerRadius(x = trackHeightPx / 2F, y = trackHeightPx / 2F)
        )

        drawCircle(
            color = activeColor,
            radius = thumbRadiusPx,
            center = Offset(x = activeWidth, y = centerY)
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun VideoProgressSliderPreview() {
    VideoProgressSlider(
        modifier = Modifier.width(240.dp),
        positionMs = 6_000L,
        durationMs = 15_000L,
        onSeek = {}
    )
}
