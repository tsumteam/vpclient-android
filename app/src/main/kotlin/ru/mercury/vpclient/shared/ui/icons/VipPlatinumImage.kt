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

val VipPlatinumImage: ImageVector
    get() {
        if (_vipPlatinumImage != null) {
            return _vipPlatinumImage!!
        }
        _vipPlatinumImage = ImageVector.Builder(
            name = "VipPlatinumImage",
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
                    """
                    M77.5872 29.0166L97.4128 29.0166C101.8701 29.0166,105.4834 32.6299,105.4834 37.0872L105.4834 56.9128C105.4834 61.3701,101.8701 64.9834,97.4128 64.9834L77.5872 64.9834C73.1299 64.9834,69.5166 61.3701,69.5166 56.9128L69.5166 37.0872C69.5166 32.6299,73.1299 29.0166,77.5872 29.0166Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M69.5166 54.8947L76.1729 48.4981L82.1673 54.4926L93.4069 43.2529L105.308 54.4926C105.834 63.3162 102.15 64.8952 99.5182 64.8952C93.3837 65.0707 80.6467 64.8943 75.8327 64.8952C73.815 64.8956 71.1833 62.6219 70.5693 61.3266C69.5166 59.1058 69.5166 57.5268 69.5166 54.8947Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFDAD8E7)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M82.2544 36.5096C84.3236 36.5096,86.0009 38.1869,86.0009 40.2561C86.0009 42.3253,84.3236 44.0026,82.2544 44.0026C80.1852 44.0026,78.5079 42.3253,78.5079 40.2561C78.5079 38.1869,80.1852 36.5096,82.2544 36.5096Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFBCB8C9)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M82.2362 54.3689L93.2894 43.3157L99.6055 64.8958H78.5518L82.2362 54.3689Z"
                ),
                fill = SolidColor(Color(0xFFCCCAD8)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M105.396 54.3689L93.29 43.3157L99.6062 64.8958C99.6062 64.8958 103.817 64.8958 104.87 61.7377C105.652 59.3896 105.396 54.3689 105.396 54.3689Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF9B99A9)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _vipPlatinumImage!!
    }

private var _vipPlatinumImage: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun VipPlatinumImagePreview() {
    Icon(
        imageVector = VipPlatinumImage,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
