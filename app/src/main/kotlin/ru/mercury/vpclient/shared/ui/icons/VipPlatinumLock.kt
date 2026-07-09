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

val VipPlatinumLock: ImageVector
    get() {
        if (_vipPlatinumLock != null) {
            return _vipPlatinumLock!!
        }
        _vipPlatinumLock = ImageVector.Builder(
            name = "VipPlatinumLock",
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
                    M76.4178 40V31.7725C76.4178 25.9491 81.3899 21.2111 87.5 21.2111C93.6101 21.2111 98.5822 25.9491 98.5822 31.7725V40H103V31.7725C103 23.628 96.047 17 87.5 17C78.953 17 72 23.628 72 31.7725V40H76.4178Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF9A96B3)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M105.21 39.9189H70C67.7909 39.9189 66 41.7098 66 43.9189V68.9138C66 71.1229 67.7909 72.9138 70 72.9138H105.21C107.419 72.9138 109.21 71.1229 109.21 68.9138V43.9189C109.21 41.7098 107.419 39.9189 105.21 39.9189Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFAF9FF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M87.5427 39.8945H105C107.209 39.8945 109 41.6854 109 43.8945V69.0001C109 71.2092 107.209 73.0001 105 73.0001H87.5427V39.8945Z"
                ),
                fill = SolidColor(Color(0xFFDAD8E7)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M106 73V40C107.657 40 109 41.3431 109 43V70C109 71.6569 107.657 73 106 73Z"
                ),
                fill = SolidColor(Color(0x99B0ABBF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M69 73V40C67.3431 40 66 41.3431 66 43V70C66 71.6569 67.3431 73 69 73Z"
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M85.3162 55.9295C85.335 55.7372 85.2338 55.5549 85.0699 55.4527C85.2338 55.5549 85.335 55.7372 85.3162 55.9295ZM89.5031 55.9291C89.4845 55.737 89.5856 55.5548 89.7494 55.4527C89.5857 55.5548 89.4843 55.7371 89.5031 55.9291Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF5D636C)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M91.7529 51.8693C91.7192 50.7398 91.2469 49.6679 90.436 48.8809C89.6252 48.0939 88.5396 47.6537 87.4097 47.6537C86.2797 47.6537 85.1942 48.0939 84.3833 48.8809C83.5725 49.6679 83.1001 50.7398 83.0664 51.8693C83.0711 52.6413 83.2858 53.3975 83.6876 54.0568C84.0339 54.6249 84.5078 55.1025 85.0699 55.4527C85.2338 55.5549 85.335 55.7372 85.3162 55.9295L84.6673 62.5831C84.6402 62.8619 84.8593 63.1034 85.1394 63.1034H89.6799C89.9601 63.1034 90.1792 62.8619 90.152 62.5831L89.5031 55.9295C89.5031 55.9294 89.5031 55.9293 89.5031 55.9291C89.4843 55.7371 89.5857 55.5548 89.7494 55.4527C90.3116 55.1025 90.7855 54.6249 91.1317 54.0568C91.5335 53.3975 91.7483 52.6413 91.7529 51.8693Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF5D636C)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _vipPlatinumLock!!
    }

private var _vipPlatinumLock: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun VipPlatinumLockPreview() {
    Icon(
        imageVector = VipPlatinumLock,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
