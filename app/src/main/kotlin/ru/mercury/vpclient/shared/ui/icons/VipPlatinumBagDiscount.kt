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

val VipPlatinumBagDiscount: ImageVector
    get() {
        if (_vipPlatinumBagDiscount != null) {
            return _vipPlatinumBagDiscount!!
        }
        _vipPlatinumBagDiscount = ImageVector.Builder(
            name = "VipPlatinumBagDiscount",
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
                    M65 60L109 60C110.1046 60,111 60.8954,111 62L111 72C111 73.1046,110.1046 74,109 74L65 74C63.8954 74,63 73.1046,63 72L63 62C63 60.8954,63.8954 60,65 60Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFDDD9E8)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M69 22L105 22C105.5523 22,106 22.4477,106 23L106 67C106 67.5523,105.5523 68,105 68L69 68C68.4477 68,68 67.5523,68 67L68 23C68 22.4477,68.4477 22,69 22Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFB4ADCC)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M69.8551 22.4625C69.3305 21.5973 68 21.9692 68 22.981V32H72V26.2795C72 26.0966 71.9499 25.9173 71.8551 25.761L69.8551 22.4625Z"
                ),
                fill = SolidColor(Color(0xFFD1CCE2)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M104.145 22.4625C104.669 21.5973 106 21.9692 106 22.981V32H102V26.2795C102 26.0966 102.05 25.9173 102.145 25.761L104.145 22.4625Z"
                ),
                fill = SolidColor(Color(0xFFD1CCE2)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M70.289 25.6522C70.9173 25.0168 72 25.4618 72 26.3553V32H65V31L70.289 25.6522Z"
                ),
                fill = SolidColor(Color(0xFFE2DEEF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M103.711 25.6522C103.083 25.0168 102 25.4618 102 26.3553V32H109V31L103.711 25.6522Z"
                ),
                fill = SolidColor(Color(0xFFE2DEEF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M65.5506 42.022C59.8882 51.9736 56.5 54.7095 56.5 50.5C56.5 46.4435 59.9917 40.6366 65.5506 33.9822"
                ),
                fill = SolidColor(Color(0x00000000)),
                pathFillType = PathFillType.NonZero,
                stroke = SolidColor(Color(0xFF5D636C)),
                strokeLineWidth = 2F,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Miter
            )
            addPath(
                pathData = addPathNodes(
                    "M108.5 42.022C114.163 51.9736 117.551 54.7095 117.551 50.5C117.551 46.4435 114.059 40.6366 108.5 33.9822"
                ),
                fill = SolidColor(Color(0x00000000)),
                pathFillType = PathFillType.NonZero,
                stroke = SolidColor(Color(0xFF5D636C)),
                strokeLineWidth = 2F,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Miter
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M74 20.4C74 18.1598 74 17.0397 74.436 16.184C74.8195 15.4314 75.4314 14.8195 76.184 14.436C77.0397 14 78.1598 14 80.4 14H93.6C95.8402 14 96.9603 14 97.816 14.436C98.5686 14.8195 99.1805 15.4314 99.564 16.184C100 17.0397 100 18.1598 100 20.4V34.0941C100 36.3343 100 37.4544 99.564 38.31C99.1805 39.0627 98.5686 39.6746 97.816 40.0581C96.9603 40.4941 95.8402 40.4941 93.6 40.4941H80.4C78.1598 40.4941 77.0397 40.4941 76.184 40.0581C75.4314 39.6746 74.8195 39.0627 74.436 38.31C74 37.4544 74 36.3343 74 34.0941L74 20.4Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF97A0AE)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M86.8203 21.8936C86.8202 24.0357 85.0835 25.7724 82.9414 25.7725C80.7993 25.7725 79.0626 24.0357 79.0625 21.8936C79.0625 19.7514 80.7992 18.0146 82.9414 18.0146C85.0835 18.0147 86.8203 19.7514 86.8203 21.8936ZM85.3652 21.8936C85.3652 20.5547 84.2802 19.4688 82.9414 19.4687C81.6025 19.4687 80.5166 20.5547 80.5166 21.8936C80.5167 23.2324 81.6026 24.3174 82.9414 24.3174C84.2802 24.3173 85.3652 23.2323 85.3652 21.8936ZM93.3428 19.292C93.6267 19.576 93.6267 20.0363 93.3428 20.3203L82.0293 31.6348C81.7454 31.9185 81.285 31.9183 81.001 31.6348C80.717 31.3508 80.717 30.8895 81.001 30.6055L92.3145 19.292C92.5984 19.0081 93.0588 19.0081 93.3428 19.292ZM95.0625 29.166C95.0625 31.3082 93.3258 33.0449 91.1836 33.0449C89.0415 33.0449 87.3047 31.3081 87.3047 29.166C87.3048 27.024 89.0415 25.2872 91.1836 25.2871C93.3257 25.2871 95.0624 27.0239 95.0625 29.166ZM93.6084 29.166C93.6083 27.8272 92.5224 26.7422 91.1836 26.7422C89.8448 26.7423 88.7599 27.8273 88.7598 29.166C88.7598 30.5048 89.8448 31.5908 91.1836 31.5908C92.5224 31.5908 93.6084 30.5049 93.6084 29.166Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M65 31H109V69C109 69.5523 108.552 70 108 70H66C65.4477 70 65 69.5523 65 69V31Z"
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M79.0879 65.5H95.5002"
                ),
                fill = SolidColor(Color(0x00000000)),
                pathFillType = PathFillType.NonZero,
                stroke = SolidColor(Color(0xFFDFDDE8)),
                strokeLineWidth = 1F,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Miter
            )
            addPath(
                pathData = addPathNodes(
                    "M73.0859 65.5H76.0486H73.0859Z"
                ),
                fill = SolidColor(Color(0x00000000)),
                pathFillType = PathFillType.NonZero,
                stroke = SolidColor(Color(0xFFDFDDE8)),
                strokeLineWidth = 1F,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            )
            addPath(
                pathData = addPathNodes(
                    "M69.5918 65.5H70.5433H69.5918Z"
                ),
                fill = SolidColor(Color(0x00000000)),
                pathFillType = PathFillType.NonZero,
                stroke = SolidColor(Color(0xFFDFDDE8)),
                strokeLineWidth = 1F,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            )
            addPath(
                pathData = addPathNodes(
                    "M103.592 65.5H104.543H103.592Z"
                ),
                fill = SolidColor(Color(0x00000000)),
                pathFillType = PathFillType.NonZero,
                stroke = SolidColor(Color(0xFFDFDDE8)),
                strokeLineWidth = 1F,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            )
        }.build()
        return _vipPlatinumBagDiscount!!
    }

private var _vipPlatinumBagDiscount: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun VipPlatinumBagDiscountPreview() {
    Icon(
        imageVector = VipPlatinumBagDiscount,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
