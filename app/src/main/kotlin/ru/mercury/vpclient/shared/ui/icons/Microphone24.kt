package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

val Microphone24: ImageVector
    get() {
        if (_microphone24 != null) {
            return _microphone24!!
        }
        _microphone24 = ImageVector.Builder(
            name = "Microphone24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M11.9998 0C9.2412 0 6.99977 2.12345 6.99977 4.73683V12.3158C6.99977 14.9291 9.2412 17.0526 11.9998 17.0526C14.7583 17.0526 16.9998 14.9291 16.9998 12.3158V4.73683C16.9998 2.12345 14.7583 0 11.9998 0ZM11.9998 1.26304C14.0426 1.26304 15.6666 2.80144 15.6666 4.73683V12.3158C15.6666 14.2511 14.0427 15.7895 11.9998 15.7895C9.95685 15.7895 8.33299 14.2512 8.33299 12.3158V4.73683C8.33299 2.8015 9.95685 1.26304 11.9998 1.26304ZM4.67715 8.52618C4.32117 8.5209 3.99448 8.83038 4.00007 9.16763V12.3255C4.00007 16.302 7.2234 19.556 11.3334 19.8751V22.737H8.66668C8.29837 22.737 8.00007 23.0198 8.00007 23.3685C8.00007 23.7174 8.29862 24 8.66668 24H15.3333C15.7016 24 15.9999 23.7172 15.9999 23.3685C15.9999 23.0196 15.7014 22.737 15.3333 22.737H12.6666V19.8751C16.7766 19.5562 19.9999 16.3023 19.9999 12.3255V9.16763C20.0048 8.83391 19.6856 8.52709 19.3333 8.52709C18.9811 8.52709 18.6616 8.83393 18.6667 9.16763V12.3255C18.6667 15.8354 15.7049 18.6413 12.0001 18.6413C8.29519 18.6413 5.3334 15.8354 5.3334 12.3255V9.16763C5.33898 8.83699 5.02625 8.53126 4.67727 8.52618H4.67715Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF6F757E)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _microphone24!!
    }

private var _microphone24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Microphone24Preview() {
    Icon(
        imageVector = Microphone24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
