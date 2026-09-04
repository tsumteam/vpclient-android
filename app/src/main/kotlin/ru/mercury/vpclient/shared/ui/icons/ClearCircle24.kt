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

val ClearCircle24: ImageVector
    get() {
        if (_clearCircle24 != null) {
            return _clearCircle24!!
        }
        _clearCircle24 = ImageVector.Builder(
            name = "ClearCircle24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M12,1C18.0751,1 23,5.92487 23,12C23,18.0751 18.0751,23 12,23C5.92487,23 1,18.0751 1,12
                    C1,5.92487 5.92487,1 12,1ZM15.5352,8.46484C15.3399,8.26958 15.0234,8.26958 14.8281,8.46484
                    L12,11.293L9.17188,8.46484C8.97661,8.26958 8.66011,8.26958 8.46484,8.46484
                    C8.26958,8.66011 8.26958,8.97661 8.46484,9.17188L11.293,12L8.46484,14.8281
                    C8.26958,15.0234 8.26958,15.3399 8.46484,15.5352C8.66011,15.7304 8.97661,15.7304 9.17188,15.5352
                    L12,12.707L14.8281,15.5352C15.0234,15.7304 15.3399,15.7304 15.5352,15.5352
                    C15.7304,15.3399 15.7304,15.0234 15.5352,14.8281L12.707,12L15.5352,9.17188
                    C15.7304,8.97661 15.7304,8.66011 15.5352,8.46484Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF97A0AE)),
                pathFillType = PathFillType.EvenOdd
            )
        }.build()
        return _clearCircle24!!
    }

private var _clearCircle24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun ClearCircle24Preview() {
    Icon(
        imageVector = ClearCircle24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
