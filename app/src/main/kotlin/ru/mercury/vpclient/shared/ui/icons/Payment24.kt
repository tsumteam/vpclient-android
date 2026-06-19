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

val Payment24: ImageVector
    get() {
        if (_payment24 != null) {
            return _payment24!!
        }
        _payment24 = ImageVector.Builder(
            name = "Payment24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M20.7083 4.66669H3.29164C2.0285 4.66669 1 5.69518 1 6.95837V17.0417C1 18.3048
                    2.0285 19.3333 3.29164 19.3333H20.7082C21.9714 19.3333 22.9999 18.3048 22.9999
                    17.0417V6.95837C22.9999 5.69518 21.9714 4.66669 20.7083 4.66669ZM22.0833
                    17.0416C22.0833 17.7997 21.4664 18.4166 20.7083 18.4166H3.29164C2.53355
                    18.4166 1.91665 17.7997 1.91665 17.0416V6.95837C1.91665 6.20028 2.53355
                    5.58338 3.29164 5.58338H20.7082C21.4663 5.58338 22.0832 6.20028 22.0832
                    6.95837V17.0416H22.0833Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M22.5416 7.41663H1.45835C1.20535 7.41663 1 7.62197 1 7.87497V10.625C1 10.878
                    1.20535 11.0833 1.45835 11.0833H22.5416C22.7946 11.0833 23 10.878 23
                    10.625V7.87497C22.9999 7.62197 22.7946 7.41663 22.5416 7.41663ZM22.0833
                    10.1666H1.91665V8.33328H22.0832V10.1666H22.0833Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M9.70832 13.8333H4.20835C3.95535 13.8333 3.75 14.0387 3.75 14.2917C3.75 14.5447
                    3.95535 14.75 4.20835 14.75H9.70832C9.96132 14.75 10.1667 14.5446 10.1667
                    14.2916C10.1667 14.0386 9.96132 13.8333 9.70832 13.8333Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M9.70832 15.6666H4.20835C3.95535 15.6666 3.75 15.872 3.75 16.125C3.75 16.378
                    3.95535 16.5833 4.20835 16.5833H9.70832C9.96132 16.5833 10.1667 16.378
                    10.1667 16.125C10.1666 15.872 9.96132 15.6666 9.70832 15.6666Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes("M1 8H23V11H1V8Z"),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _payment24!!
    }

private var _payment24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Payment24Preview() {
    Icon(
        imageVector = Payment24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
