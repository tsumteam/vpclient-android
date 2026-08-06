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

val TextFieldError24: ImageVector
    get() {
        if (_textFieldError24 != null) {
            return _textFieldError24!!
        }
        _textFieldError24 = ImageVector.Builder(
            name = "TextFieldError24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M12,24C18.6274,24 24,18.6274 24,12C24,5.37258 18.6274,0 12,0C5.37258,0 0,5.37258 0,12
                    C0,18.6274 5.37258,24 12,24Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFD76B6B)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M12,6C11.4477,6 11,6.44772 11,7V14C11,14.5523 11.4477,15 12,15C12.5523,15 13,14.5523 13,14V7
                    C13,6.44772 12.5523,6 12,6ZM12,18C12.5523,18 13,17.5523 13,17C13,16.4477 12.5523,16 12,16
                    C11.4477,16 11,16.4477 11,17C11,17.5523 11.4477,18 12,18Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _textFieldError24!!
    }

private var _textFieldError24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun TextFieldError24Preview() {
    Icon(
        imageVector = TextFieldError24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
