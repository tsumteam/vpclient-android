package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

val Logout24: ImageVector
    get() {
        if (_logout24 != null) {
            return _logout24!!
        }
        _logout24 = ImageVector.Builder(
            name = "Logout24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M8.75497 18C9.03111 18 9.25497 18.2239 9.25497 18.5L9.25497 19.0977C9.25497 19.0977 9.26197
                    19.2664 9.31052 19.4681C9.48633 20.1984 10.0566 20.7686 10.7869 20.9445C10.9886 20.993
                    11.2605 21 12.1573 21L17.455 21C18.3116 21 18.8938 20.9992 19.3439 20.9624C19.7823
                    20.9266 20.0066 20.8617 20.163 20.782C20.5393 20.5903 20.8452 20.2843 21.037 19.908C21.1167
                    19.7516 21.1816 19.5274 21.2174 19.089C21.2542 18.6389 21.255 18.0566 21.255 17.2L21.255
                    6.8C21.255 5.94342 21.2542 5.36113 21.2174 4.91104C21.1816 4.47262 21.1167 4.24842
                    21.037 4.09202C20.8452 3.71569 20.5393 3.40973 20.163 3.21799C20.0066 3.1383 19.7823
                    3.07337 19.3439 3.03755C18.8938 3.00078 18.3116 3 17.455 3L12.2321 3C11.2606 3
                    10.9664 3.0082 10.7489 3.06508C10.0493 3.24805 9.50302 3.79438 9.32005 4.49394C9.27433
                    4.66877 9.26006 4.90795 9.25622 5.49969C9.25443 5.77582 9.03111 6 8.75497 6C8.47883
                    6 8.25497 5.77614 8.25497 5.5L8.25497 4.97712C8.255 4.5 8.31323 4.39155 8.35259
                    4.24091C8.62704 3.19156 9.44654 2.37207 10.4959 2.09762C10.8692 2 11.3235 2 12.2321
                    2L17.455 2C19.1351 2 19.9752 2 20.6169 2.32698C21.1814 2.6146 21.6404 3.07354
                    21.928 3.63803C22.255 4.27976 22.255 5.11984 22.255 6.8L22.255 17.2C22.255 18.8802
                    22.255 19.7202 21.928 20.362C21.6404 20.9265 21.1814 21.3854 20.6169 21.673C19.9752 22
                    19.1351 22 17.455 22L12.1573 22C11.3184 22 10.899 22 10.5528 21.9167C9.45735 21.653
                    8.60201 20.7976 8.33829 19.7021C8.25497 19.356 8.25497 18.9023 8.25497 19L8.25497 18.5C8.25497
                    18.2239 8.47883 18 8.75497 18Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1B1B1B)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M14.7686 11.5C15.0447 11.5 15.2686 11.7239 15.2686 12C15.2686 12.2761 15.0447 12.5
                    14.7686 12.5L14.7686 12L14.7686 11.5ZM2.72363 12.5L2.22363 12.5L2.22363 11.5L2.72363
                    11.5L2.72363 12L2.72363 12.5ZM14.7686 12L14.7686 12.5L2.72363 12.5L2.72363 12L2.72363
                    11.5L14.7686 11.5L14.7686 12Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1B1B1B)),
                pathFillType = PathFillType.NonZero
            )
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFF1B1B1B)),
                strokeLineWidth = 1F,
                strokeLineCap = StrokeCap.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(5.50815F, 8.46484F)
                lineTo(2F, 11.973F)
                curveTo(2F, 11.973F, 4.13813F, 14.0678F, 5.50815F, 15.41F)
            }
        }.build()
        return _logout24!!
    }

private var _logout24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Logout24Preview() {
    Icon(
        imageVector = Logout24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
