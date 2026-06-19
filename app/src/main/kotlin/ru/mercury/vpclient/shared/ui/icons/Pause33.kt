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

val Pause33: ImageVector
    get() {
        if (_pause33 != null) {
            return _pause33!!
        }
        _pause33 = ImageVector.Builder(
            name = "Pause33",
            defaultWidth = 33.dp,
            defaultHeight = 37.dp,
            viewportWidth = 33F,
            viewportHeight = 37F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M33 35C33 36.1046 32.1046 37 31 37H21.0385C19.9339 37 19.0385 36.1046 19.0385 35V2C19.0385 0.895431 19.9339 4.84937e-08 21.0385 1.08315e-07L31 6.47812e-07C32.1046 7.07633e-07 33 0.895431 33 2L33 35Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M13.3269 35C13.3269 36.1046 12.4315 37 11.3269 37H2C0.895429 37 1.01818e-07 36.1046 2.27417e-07 35L3.9798e-06 2C4.1054e-06 0.895432 0.895435 5.08039e-08 2 1.13474e-07L11.3269 6.42654e-07C12.4315 7.05323e-07 13.3269 0.895431 13.3269 2V18.5V35Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _pause33!!
    }

private var _pause33: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Pause33Preview() {
    Icon(
        imageVector = Pause33,
        contentDescription = null,
        tint = Color.Black
    )
}
