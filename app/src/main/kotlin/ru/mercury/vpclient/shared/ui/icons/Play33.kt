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

val Play33: ImageVector
    get() {
        if (_play33 != null) {
            return _play33!!
        }
        _play33 = ImageVector.Builder(
            name = "Play33",
            defaultWidth = 33.dp,
            defaultHeight = 37.dp,
            viewportWidth = 33F,
            viewportHeight = 37F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M3.13304 37C4.03998 37 4.84385 36.6899 5.87445 36.0905L30.341 21.8693C32.1755 20.8151 33 19.9263 33 18.5C33 17.0944 32.1755 16.2056 30.341 15.1307L5.87445 0.909497C4.84385 0.310056 4.03998 0 3.13304 0C1.33979 0 0 1.38492 0 3.59665V33.4034C0 35.6358 1.33979 37 3.13304 37Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _play33!!
    }

private var _play33: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Play33Preview() {
    Icon(
        imageVector = Play33,
        contentDescription = null,
        tint = Color.Black
    )
}
