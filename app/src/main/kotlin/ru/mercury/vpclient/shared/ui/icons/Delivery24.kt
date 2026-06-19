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

val Delivery24: ImageVector
    get() {
        if (_delivery24 != null) {
            return _delivery24!!
        }
        _delivery24 = ImageVector.Builder(
            name = "Delivery24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M5.6002 15.8C4.27659 15.8 3.2002 16.8764 3.2002 18.2C3.2002 19.5236 4.27659
                    20.6 5.6002 20.6C6.9238 20.6 8.0002 19.5236 8.0002 18.2C8.0002 16.8764
                    6.92376 15.8 5.6002 15.8ZM5.6002 19.8C4.71777 19.8 4.00021 19.0824
                    4.00021 18.2C4.00021 17.3176 4.71782 16.6 5.6002 16.6C6.48257 16.6
                    7.20018 17.3176 7.20018 18.2C7.20018 19.0824 6.48257 19.8 5.6002 19.8Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M19.1998 15.8C17.8762 15.8 16.7998 16.8764 16.7998 18.2C16.7998 19.5236 17.8762
                    20.6 19.1998 20.6C20.5234 20.6 21.5998 19.5236 21.5998 18.2C21.5998 16.8764
                    20.5234 15.8 19.1998 15.8ZM19.1998 19.8C18.3174 19.8 17.5998 19.0824
                    17.5998 18.2C17.5998 17.3176 18.3174 16.6 19.1998 16.6C20.0822 16.6
                    20.7998 17.3176 20.7998 18.2C20.7998 19.0824 20.0822 19.8 19.1998 19.8Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M23.9278 13.8907L19.8368 8.18701C19.7532 8.07047 19.6091 8 19.4545 8H14.4545C14.2032
                    8 14 8.18252 14 8.40739V18.5926C14 18.8179 14.2032 19 14.4545 19H17.2727V18H15V9H19.2656L23
                    14.1758V18H21V19H23.5455C23.7968 19 24 18.8179 24 18.5926V14.1111C24.0001 14.0333
                    23.9751 13.9563 23.9278 13.8907Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M14.625 5H0.374986C0.168003 5 0 5.21083 0 5.47057V18.5294C0 18.7897 0.168003
                    19 0.374986 19H3.375V18H1V6H14V18H7.5V19H14.625C14.8324 19 15 18.7897
                    15 18.5294V5.47057C15 5.21083 14.8324 5 14.625 5Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _delivery24!!
    }

private var _delivery24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Delivery24Preview() {
    Icon(
        imageVector = Delivery24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
