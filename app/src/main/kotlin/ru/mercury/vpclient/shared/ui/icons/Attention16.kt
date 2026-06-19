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

val Attention16: ImageVector
    get() {
        if (_attention16 != null) {
            return _attention16!!
        }
        _attention16 = ImageVector.Builder(
            name = "Attention16",
            defaultWidth = 16.dp,
            defaultHeight = 16.dp,
            viewportWidth = 16F,
            viewportHeight = 16F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M8,16C12.4183,16 16,12.4183 16,8C16,3.5817 12.4183,0 8,0C3.5817,0 0,3.5817 0,8
                    C0,12.4183 3.5817,16 8,16Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M8,3.2084C8.5098,3.2084 8.9229,3.6215 8.9229,4.1315V9.2853
                    C8.9229,9.7954 8.5098,10.2084 8,10.2084C7.4902,10.2084 7.0767,9.7954 7.0767,9.2853V4.1315
                    C7.0767,3.6215 7.4902,3.2084 8,3.2084Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFD76B6B)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M8,11C8.5523,11 9,11.4477 9,12C9,12.5523 8.5523,13 8,13
                    C7.4477,13 7,12.5523 7,12C7,11.4477 7.4477,11 8,11Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFD76B6B)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _attention16!!
    }

private var _attention16: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Attention16Preview() {
    Icon(
        imageVector = Attention16,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
