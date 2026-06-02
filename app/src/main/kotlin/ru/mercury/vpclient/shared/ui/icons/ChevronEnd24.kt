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
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

val ChevronEnd24: ImageVector
    get() {
        if (_chevronEnd24 != null) {
            return _chevronEnd24!!
        }
        _chevronEnd24 = ImageVector.Builder(
            name = "ChevronEnd24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M18,11.9407C18,11.6815 17.9135,11.5085 17.7408,11.3356L8.66451,2.25926
                    C8.31883,1.91358 7.71369,1.91358 7.28154,2.25926C6.93586,2.60494 6.93586,3.21008
                    7.28154,3.64223L15.752,11.9402L7.36779,20.3244C7.02211,20.6701 7.02211,21.2753
                    7.36779,21.7074C7.71347,22.1396 8.31862,22.0531 8.75077,21.7074L17.7401,12.6318
                    C17.9131,12.4588 17.9996,12.1994 17.9996,11.9401L18,11.9407Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF000000)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _chevronEnd24!!
    }

private var _chevronEnd24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun ChevronEnd24Preview() {
    Icon(
        imageVector = ChevronEnd24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
