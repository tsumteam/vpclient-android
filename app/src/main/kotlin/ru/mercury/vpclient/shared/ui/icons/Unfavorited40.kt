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

val Unfavorited40: ImageVector
    get() {
        if (_unfavorited40 != null) {
            return _unfavorited40!!
        }
        _unfavorited40 = ImageVector.Builder(
            name = "Unfavorited40",
            defaultWidth = 40.dp,
            defaultHeight = 40.dp,
            viewportWidth = 40F,
            viewportHeight = 40F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M28.8047,3.5C34.8094,3.5 38.5,8.64534 38.5,12.8428C38.4998,17.4478 35.8196,21.9526 32.0791,26.0557
                    C28.4411,30.0463 23.8657,33.5834 20,36.3809C16.1344,33.5835 11.5589,30.0462 7.9209,26.0557
                    C4.18049,21.9527 1.50016,17.4482 1.5,12.8428C1.5,8.7762 4.96304,3.8205 10.6387,3.51465
                    L11.1953,3.5C12.8258,3.5 14.3643,4.13554 15.79,5.11523C17.2166,6.09551 18.5033,7.40156 19.6221,8.69531
                    L20,9.13281L20.3779,8.69531C21.4968,7.40149 22.7843,6.09554 24.2109,5.11523
                    C25.6366,4.13569 27.1744,3.50007 28.8047,3.5Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFF1B1B1B)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _unfavorited40!!
    }

private var _unfavorited40: ImageVector? = null

@FontScalePreviews
@Composable
private fun Unfavorited40Preview() {
    ClientTheme {
        Icon(
            imageVector = Unfavorited40,
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}
