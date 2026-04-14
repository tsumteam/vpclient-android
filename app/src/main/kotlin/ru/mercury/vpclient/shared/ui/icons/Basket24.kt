package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientTheme

val Basket24: ImageVector
    get() {
        if (_basket24 != null) {
            return _basket24!!
        }
        _basket24 = ImageVector.Builder(
            name = "Basket24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M3.936,7.5C3.809,7.5 3.687,7.548 3.594,7.635C3.501,7.721 3.445,7.84 3.436,7.967
                    L2.57,20.967L2.569,21C2.569,21.133 2.622,21.26 2.716,21.354C2.81,21.447 2.937,21.5
                    3.069,21.5H20.93L20.963,21.499C21.029,21.495 21.093,21.478 21.152,21.448C21.211,21.419
                    21.263,21.379 21.306,21.33C21.35,21.28 21.383,21.223 21.404,21.16C21.425,21.098
                    21.434,21.032 21.429,20.967L20.562,7.967C20.554,7.84 20.498,7.722 20.405,7.635C20.312,7.548
                    20.19,7.5 20.063,7.5L3.936,7.5V7.5Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M11.993,1.99C15.912,1.99 17.313,7.253 17.476,10.38C17.5,10.447 17.509,10.519 17.503,10.59
                    C17.497,10.661 17.475,10.729 17.44,10.792C17.406,10.854 17.358,10.908 17.3,10.95C17.243,10.992
                    17.177,11.021 17.107,11.036C17.038,11.051 16.966,11.05 16.896,11.035C16.826,11.019 16.761,10.989
                    16.704,10.946C16.647,10.904 16.6,10.849 16.566,10.787C16.532,10.724 16.511,10.655 16.506,10.584
                    L16.507,10.588C16.389,8.312 15.323,3.022 11.993,3.022C8.66,3.022 7.618,8.307 7.504,10.59
                    C7.49,10.722 7.425,10.842 7.323,10.927C7.222,11.011 7.091,11.053 6.959,11.042C6.893,11.036
                    6.83,11.018 6.771,10.988C6.713,10.957 6.661,10.916 6.619,10.865C6.577,10.815 6.545,10.757
                    6.525,10.694C6.505,10.631 6.498,10.566 6.504,10.5L6.505,10.466C6.647,7.35 8.038,1.99 11.993,1.99Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _basket24!!
    }

private var _basket24: ImageVector? = null

@FontScalePreviews
@Composable
private fun Basket24Preview() {
    ClientTheme {
        Icon(
            imageVector = Basket24,
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}
