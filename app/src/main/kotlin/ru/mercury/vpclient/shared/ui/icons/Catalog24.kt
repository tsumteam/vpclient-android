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

val Catalog24: ImageVector
    get() {
        if (_catalog24 != null) {
            return _catalog24!!
        }
        _catalog24 = ImageVector.Builder(
            name = "Catalog24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M7.78125 22.9004C8.10573 22.9004 8.36914 22.6369 8.36914 22.3125V6.5C8.36914 6.17555 8.10573 5.91211 7.78125 5.91211C7.45677 5.91211 7.19336 6.17555 7.19336 6.5V22.3125C7.19336 22.6369 7.45677 22.9004 7.78125 22.9004Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1B1B1B)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M6.83594 22.9004H19.125C19.4495 22.9004 19.7129 22.6369 19.7129 22.3125V6.5C19.7129 6.17555 19.4495 5.91211 19.125 5.91211C18.8005 5.91211 18.5371 6.17555 18.5371 6.5V21.7246H6.83594C5.97513 21.7246 5.27539 21.0249 5.27539 20.1641V4.09375C5.27539 3.7693 5.01198 3.50586 4.6875 3.50586C4.36302 3.50586 4.09961 3.7693 4.09961 4.09375V20.1641C4.09961 21.6726 5.3274 22.9004 6.83594 22.9004Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1B1B1B)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M7.09375 7.08789H19.125C19.4495 7.08789 19.7129 6.82445 19.7129 6.5C19.7129 6.17555 19.4495 5.91211 19.125 5.91211H7.09375C6.0908 5.91211 5.27539 5.0967 5.27539 4.09375C5.27539 3.0908 6.0908 2.27539 7.09375 2.27539H19.125C19.4495 2.27539 19.7129 2.01195 19.7129 1.6875C19.7129 1.36305 19.4495 1.09961 19.125 1.09961H7.09375C5.44308 1.09961 4.09961 2.44308 4.09961 4.09375C4.09961 5.74442 5.44308 7.08789 7.09375 7.08789Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1B1B1B)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M7.09375 4.68164H19.125C19.4495 4.68164 19.7129 4.4182 19.7129 4.09375C19.7129 3.7693 19.4495 3.50586 19.125 3.50586H7.09375C6.76927 3.50586 6.50586 3.7693 6.50586 4.09375C6.50586 4.4182 6.76927 4.68164 7.09375 4.68164Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1B1B1B)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _catalog24!!
    }

private var _catalog24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Catalog24Preview() {
    Icon(
        imageVector = Catalog24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
