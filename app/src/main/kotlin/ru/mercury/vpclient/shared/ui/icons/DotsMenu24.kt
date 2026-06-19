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

val DotsMenu24: ImageVector
    get() {
        if (_dotsMenu24 != null) {
            return _dotsMenu24!!
        }
        _dotsMenu24 = ImageVector.Builder(
            name = "DotsMenu24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes("M20,10A2,2 0,1 1,20 14A2,2 0,1 1,20 10Z"),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes("M12,10A2,2 0,1 1,12 14A2,2 0,1 1,12 10Z"),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes("M4,10A2,2 0,1 1,4 14A2,2 0,1 1,4 10Z"),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _dotsMenu24!!
    }

private var _dotsMenu24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun DotsMenu24Preview() {
    Icon(
        imageVector = DotsMenu24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
