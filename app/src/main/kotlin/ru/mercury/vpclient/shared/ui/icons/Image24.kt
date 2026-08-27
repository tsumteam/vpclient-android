package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

val Image24: ImageVector
    get() {
        if (_image24 != null) {
            return _image24!!
        }
        _image24 = ImageVector.Builder(
            name = "Image24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M6 2.5L18 2.5A3.5 3.5 0 0 1 21.5 6L21.5 18A3.5 3.5 0 0 1 18 21.5L6 21.5A3.5 3.5 0 0 1 2.5 18L2.5 6A3.5 3.5 0 0 1 6 2.5Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFF1B1B1B)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M2.41602 16.167L5.74935 12.8337L9.08268 16.167L15.3327 9.91699L21.5827 16.167
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFF1B1B1B)),
                strokeLineWidth = 1F,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M10.66666 8.25033A1.58333 1.58333 0 1 1 7.5 8.25033A1.58333 1.58333 0 1 1 10.66666 8.25033Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFF1B1B1B)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _image24!!
    }

private var _image24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Image24Preview() {
    Icon(
        imageVector = Image24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
