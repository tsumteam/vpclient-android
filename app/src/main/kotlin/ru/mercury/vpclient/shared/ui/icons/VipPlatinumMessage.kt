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

val VipPlatinumMessage: ImageVector
    get() {
        if (_vipPlatinumMessage != null) {
            return _vipPlatinumMessage!!
        }
        _vipPlatinumMessage = ImageVector.Builder(
            name = "VipPlatinumMessage",
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
                fill = SolidColor(Color(0xFFF5F4F9)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M64 60L94 60C95.1046 60,96 60.8954,96 62L96 72C96 73.1046,95.1046 74,94 74L64 74C62.8954 74,62 73.1046,62 72L62 62C62 60.8954,62.8954 60,64 60Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFDDD9E8)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M64 22C64 19.1997 64 17.7996 64.545 16.73C65.0243 15.7892 65.7892 15.0243 66.73 14.545C67.7996 14 69.1997 14 72 14H86C88.8003 14 90.2004 14 91.27 14.545C92.2108 15.0243 92.9757 15.7892 93.455 16.73C94 17.7996 94 19.1997 94 22V62C94 64.8003 94 66.2004 93.455 67.27C92.9757 68.2108 92.2108 68.9757 91.27 69.455C90.2004 70 88.8003 70 86 70H72C69.1997 70 67.7996 70 66.73 69.455C65.7892 68.9757 65.0243 68.2108 64.545 67.27C64 66.2004 64 64.8003 64 62V22Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF97A0AE)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M72 16C72.5523 16 73 16.4477 73 17C73 18.1046 73.8954 19 75 19H83C84.1046 19 85 18.1046 85 17C85 16.4477 85.4477 16 86 16H87.2002C88.8802 16 89.7206 16.0002 90.3623 16.3271C90.9265 16.6147 91.3853 17.0735 91.6729 17.6377C91.9998 18.2794 92 19.1198 92 20.7998V63.2002C92 64.8802 91.9998 65.7206 91.6729 66.3623C91.3853 66.9265 90.9265 67.3853 90.3623 67.6729C89.7206 67.9998 88.8802 68 87.2002 68H70.7998C69.1198 68 68.2794 67.9998 67.6377 67.6729C67.0735 67.3853 66.6147 66.9265 66.3271 66.3623C66.0002 65.7206 66 64.8802 66 63.2002V20.7998C66 19.1198 66.0002 18.2794 66.3271 17.6377C66.6147 17.0735 67.0735 16.6147 67.6377 16.3271C68.2794 16.0002 69.1198 16 70.7998 16H72Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF7D8796)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M70.5 41.5L78.5 48.5L88.8362 36.7092"
                ),
                fill = SolidColor(Color(0x00000000)),
                pathFillType = PathFillType.NonZero,
                stroke = SolidColor(Color(0xFFADBACD)),
                strokeLineWidth = 2F,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Miter
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M72.5979 34.181C71.3884 32.376 72.4392 29.9239 74.5803 29.5546L93.9904 26.2073L94.0082 45.9192L87.723 52.4076C86.8372 53.3221 85.3337 53.1871 84.625 52.1294L72.5979 34.181Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xB15D636C)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M75.5 17L82.5 17C82.7761 17,83 17.2239,83 17.5L83 17.5C83 17.7761,82.7761 18,82.5 18L75.5 18C75.2239 18,75 17.7761,75 17.5L75 17.5C75 17.2239,75.2239 17,75.5 17Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF5D636C)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M84 39H98L98.095 42.1066L87.4861 52.6241C86.9005 53.2046 85.9035 52.8668 85.7913 52.0499L84 39Z"
                ),
                fill = SolidColor(Color(0xFFD1CCE2)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    "M89.5232 43.9226L98 39L98.095 42.1066L85.8564 54.2778L89.5232 43.9226Z"
                ),
                fill = SolidColor(Color(0xFFB4AECB)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M74.0386 33.3662C73.2939 32.8671 73.5314 31.7158 74.4128 31.5523L120.177 23.06C121.02 22.9035 121.656 23.8115 121.221 24.5506L100.67 59.457C100.292 60.0984 99.3712 60.1172 98.9677 59.4917L88.2659 42.9009L74.0386 33.3662Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M84.166 40.1456L103.668 32.3616C104.209 32.1456 104.619 32.8727 104.155 33.2245L89.2626 44.5032L84.166 40.1456Z"
                ),
                fill = SolidColor(Color(0xFFD1CCE2)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _vipPlatinumMessage!!
    }

private var _vipPlatinumMessage: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun VipPlatinumMessagePreview() {
    Icon(
        imageVector = VipPlatinumMessage,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
