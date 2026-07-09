package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

val VipPlatinumDone: ImageVector
    get() {
        if (_vipPlatinumDone != null) {
            return _vipPlatinumDone!!
        }
        _vipPlatinumDone = ImageVector.Builder(
            name = "VipPlatinumDone",
            defaultWidth = 175.dp,
            defaultHeight = 92.dp,
            viewportWidth = 175F,
            viewportHeight = 92F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M130.472 52C126.911 52.0065 124.226 54.6921 124.226 58.0049C124.226 61.3177 126.911 64.0033 130.224 64.0033C130.307 64.0033 140.472 64 140.472 64L151.486 63.9935C154.793 64.0101 157.472 66.693 157.472 70.0016C157.472 73.3144 154.786 76 151.473 76C151.357 76 124 75.9902 124 75.9902C123.206 75.9902 122.537 76.3278 121.995 77.003C113.573 86.2175 101.462 92 88.0004 92C62.5953 92 42.0004 71.4051 42.0004 46C41.9466 45.3 41.2794 44.9902 39.9989 44.9902H24.0907C20.4317 45 17.7461 42.3144 17.7461 39.0016C17.7461 35.693 20.4248 33.0101 23.7318 33.0033L38.7461 32.9935L48.7461 33C52.3066 33.0033 54.9922 30.3177 54.9922 27.0049C54.9922 23.818 52.5069 21.2116 49.3687 21.0181L38.345 20.9967C34.686 21.0065 32.0004 18.321 32.0004 15.0082C32.0004 11.6996 34.6791 9.01665 37.9861 9.00984C45.5899 8.95393 52.5953 8.95393 59.0021 9C59.9996 9 60.8952 8.7544 61.6888 8.26319C69.1446 3.055 78.2157 0 88.0004 0C111.062 0 130.16 16.9707 133.487 39.1049C133.625 39.7016 134.622 40 136.48 40C142.111 39.9595 143.217 42.6996 143.217 46.0082C143.217 49.2262 140.683 51.8523 137.502 52H136.936C136.915 51.999 133.613 51.9967 133.613 51.9967L130.472 52ZM6.74446 45C3.43165 45 0.746094 42.3144 0.746094 39.0016C0.746094 35.6888 3.43165 33.0033 6.74446 33.0033C10.0573 33.0033 12.7428 35.6888 12.7428 39.0016C12.7428 42.3144 10.0573 45 6.74446 45ZM168.473 76C165.161 76 162.475 73.3144 162.475 70.0016C162.475 66.6888 165.161 64.0033 168.473 64.0033C171.786 64.0033 174.472 66.6888 174.472 70.0016C174.472 73.3144 171.786 76 168.473 76Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFEFEDF5)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    "M87.5 20C101.5833 20,113 31.4167,113 45.5C113 59.5833,101.5833 71,87.5 71C73.4167 71,62 59.5833,62 45.5C62 31.4167,73.4167 20,87.5 20Z"
                ),
                fill = SolidColor(Color(0xFFFAF9FF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M74.9275 37.5212L75.8268 36.872C76.8201 36.1548,78.2067 36.3787,78.9239 37.372L91.2602 54.4584C91.9773 55.4517,91.7535 56.8384,90.7601 57.5555L89.8609 58.2048C88.8675 58.922,87.4809 58.6981,86.7637 57.7048L74.4275 40.6184C73.7103 39.625,73.9341 38.2384,74.9275 37.5212Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFDAD8E7)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M74.4275 40.6184C73.7103 39.6251 73.9342 38.2384 74.9275 37.5213L89.8609 58.2049C88.8676 58.922 87.4809 58.6982 86.7638 57.7049L74.4275 40.6184Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFB0ABBF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M98.617 43.1395L99.5114 43.7954C100.4994 44.5199,100.713 45.9082,99.9885 46.8961L91.438 58.5559C90.7135 59.5438,89.3253 59.7574,88.3373 59.0329L87.4428 58.377C86.4549 57.6525,86.2413 56.2642,86.9658 55.2762L95.5162 43.6165C96.2408 42.6285,97.629 42.4149,98.617 43.1395Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFDAD8E7)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M95.5163 43.6165C96.2408 42.6286 97.629 42.415 98.617 43.1395L87.4429 58.377C86.4549 57.6525 86.2413 56.2642 86.9658 55.2762L95.5163 43.6165Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFB0ABBF)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _vipPlatinumDone!!
    }

private var _vipPlatinumDone: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun VipPlatinumDonePreview() {
    Icon(
        imageVector = VipPlatinumDone,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
