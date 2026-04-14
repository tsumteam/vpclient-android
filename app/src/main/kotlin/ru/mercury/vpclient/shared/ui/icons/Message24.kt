package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientTheme

val Message24: ImageVector
    get() {
        if (_message24 != null) {
            return _message24!!
        }
        _message24 = ImageVector.Builder(
            name = "Message24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M21.3137,3.31372L2.31375,10.5357L10.2639,14.3635L14.0918,22.3137L21.3137,3.31372Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 1F,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M20.8641,3.76318L10.2639,14.3634
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _message24!!
    }

private var _message24: ImageVector? = null

@FontScalePreviews
@Composable
private fun Message24Preview() {
    ClientTheme {
        Icon(
            imageVector = Message24,
            contentDescription = null,
            tint = Color.Black
        )
    }
}
