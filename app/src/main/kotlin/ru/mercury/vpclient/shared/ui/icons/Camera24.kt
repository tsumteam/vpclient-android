package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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

val Camera24: ImageVector
    get() {
        if (_camera24 != null) {
            return _camera24!!
        }
        _camera24 = ImageVector.Builder(
            name = "Camera24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M9.002,3.5H15.043
                    C15.691,3.5 16.066,3.662 16.314,3.86
                    C16.575,4.069 16.749,4.36 16.928,4.724
                    C17.089,5.052 17.28,5.515 17.565,5.857
                    C17.886,6.245 18.334,6.503 18.986,6.507
                    V6.508H18.987
                    C18.99,6.508 18.997,6.508 19.005,6.509
                    C19.021,6.51 19.047,6.511 19.081,6.515
                    C19.15,6.521 19.252,6.534 19.376,6.558
                    C19.626,6.605 19.952,6.693 20.273,6.856
                    C20.591,7.02 20.894,7.252 21.117,7.583
                    C21.338,7.91 21.5,8.363 21.5,9
                    V18C21.5,19.381 20.381,20.5 19,20.5
                    H5C3.619,20.5 2.5,19.381 2.5,18
                    V9C2.5,8.362 2.663,7.909 2.884,7.582
                    C3.107,7.251 3.409,7.018 3.728,6.855
                    C4.048,6.692 4.373,6.603 4.623,6.557
                    C4.747,6.533 4.849,6.521 4.918,6.515
                    C4.952,6.511 4.979,6.51 4.995,6.509
                    C5.003,6.508 5.009,6.508 5.012,6.508
                    H5.013L5.012,6.507
                    C5.663,6.504 6.111,6.245 6.433,5.857
                    C6.716,5.517 6.91,5.054 7.073,4.728
                    C7.254,4.365 7.432,4.073 7.7,3.863
                    C7.956,3.663 8.341,3.5 9.002,3.5Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M12,8.5A4.5,4.5 0,1 1,12,17.5A4.5,4.5 0,1 1,12,8.5Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _camera24!!
    }

private var _camera24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Camera24Preview() {
    Icon(
        imageVector = Camera24,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
