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

val VipPlatinumCongratsSecondary: ImageVector
    get() {
        if (_vipPlatinumCongratsSecondary != null) {
            return _vipPlatinumCongratsSecondary!!
        }
        _vipPlatinumCongratsSecondary = ImageVector.Builder(
            name = "VipPlatinumCongratsSecondary",
            defaultWidth = 175.dp,
            defaultHeight = 92.dp,
            viewportWidth = 175F,
            viewportHeight = 92F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M130.726 52C127.165 52.0065 124.48 54.6921 124.48 58.0049C124.48 61.3177 127.165 64.0033 130.478 64.0033C130.561 64.0033 140.726 64 140.726 64L151.74 63.9935C155.047 64.0101 157.726 66.693 157.726 70.0016C157.726 73.3144 155.04 76 151.727 76C151.611 76 124.254 75.9902 124.254 75.9902C123.46 75.9902 122.791 76.3278 122.249 77.003C113.827 86.2175 101.716 92 88.2543 92C62.8492 92 42.2543 71.4051 42.2543 46C42.2005 45.3 41.5333 44.9902 40.2528 44.9902H24.3446C20.6856 45 18 42.3144 18 39.0016C18 35.693 20.6787 33.0101 23.9857 33.0033L39 32.9935L49 33C52.5606 33.0033 55.2461 30.3177 55.2461 27.0049C55.2461 23.818 52.7608 21.2116 49.6226 21.0181L38.5989 20.9967C34.9399 21.0065 32.2543 18.321 32.2543 15.0082C32.2543 11.6996 34.933 9.01665 38.24 9.00984C45.8438 8.95393 52.8492 8.95393 59.256 9C60.2535 9 61.1491 8.7544 61.9427 8.26319C69.3985 3.055 78.4696 0 88.2543 0C111.316 0 130.414 16.9707 133.741 39.1049C133.879 39.7016 134.876 40 136.734 40C142.365 39.9595 143.471 42.6996 143.471 46.0082C143.471 49.2262 140.937 51.8523 137.756 52H137.19C137.169 51.999 133.867 51.9967 133.867 51.9967L130.726 52ZM6.99836 45C3.68556 45 1 42.3144 1 39.0016C1 35.6888 3.68556 33.0033 6.99836 33.0033C10.3112 33.0033 12.9967 35.6888 12.9967 39.0016C12.9967 42.3144 10.3112 45 6.99836 45ZM168.727 76C165.415 76 162.729 73.3144 162.729 70.0016C162.729 66.6888 165.415 64.0033 168.727 64.0033C172.04 64.0033 174.726 66.6888 174.726 70.0016C174.726 73.3144 172.04 76 168.727 76Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFEFEDF5)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M89.801 46.1376C89.226 46.0482 88.6451 46.0023 88.0631 46C87.481 45.9989 86.8997 46.0449 86.3252 46.1376C83.892 46.4777 81.6199 47.5391 79.8081 49.182C78.6043 50.2717 77.6401 51.5948 76.9753 53.0692C76.8015 53.4476 76.6451 53.826 76.5061 54.2216C76.3844 54.6172 76.2802 54.9956 76.1933 55.3912C76.1933 55.7696 76.0542 56.1308 76.0195 56.492C76.0129 56.5492 76.0129 56.6069 76.0195 56.664C75.9935 57.0936 75.9935 57.5244 76.0195 57.954C76.0247 58.6868 76.0945 59.4177 76.228 60.1384C76.228 60.1384 76.228 60.2244 76.228 60.276C76.701 62.6467 77.8839 64.8216 79.6227 66.5179C81.3615 68.2143 83.5758 69.3536 85.9776 69.7876C86.6622 69.9486 87.3603 70.0465 88.0631 70.08C90.8651 70.0814 93.5817 69.1254 95.7539 67.3736C97.9261 65.6218 99.4206 63.1816 99.9851 60.4652C99.9797 60.385 99.9797 60.3046 99.9851 60.2244C100.126 59.5041 100.208 58.7735 100.228 58.04C100.254 57.6104 100.254 57.1796 100.228 56.75C100.236 56.6987 100.236 56.6465 100.228 56.5952C100.228 56.2168 100.124 55.8556 100.055 55.4772C99.9677 55.0816 99.8634 54.7032 99.7417 54.3076C99.6027 53.912 99.4463 53.5336 99.2725 53.1552C98.6078 51.6808 97.6435 50.3577 96.4397 49.268C94.6061 47.5717 92.2872 46.4783 89.801 46.1376Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF9A96B3)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M88.3348 50.2532L90.0624 55.8315H95.6503C95.9885 55.8315 96.1283 56.2847 95.8565 56.4945L91.3338 59.9413L93.0613 65.5195C93.1652 65.8572 92.7974 66.1366 92.5232 65.9284L88.0005 62.4816L83.4778 65.9284C83.2037 66.1358 82.8358 65.8572 82.9397 65.5195L84.6672 59.9413L80.1445 56.4945C79.8704 56.2871 80.0125 55.8315 80.3507 55.8315H85.9387L87.6662 50.2532C87.7701 49.9156 88.2262 49.9156 88.3301 50.2532H88.3348Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M112.407 27.86C112.255 27.6 112.038 27.3839 111.775 27.233C111.513 27.0821 111.216 27.0018 110.912 27H100.485C100.182 27.0018 99.8842 27.0821 99.622 27.233C99.3598 27.3839 99.1421 27.6 98.9904 27.86L89.293 44.286C89.6342 44.2598 89.9771 44.2598 90.3183 44.286C93.0842 44.6542 95.6688 45.8551 97.7218 47.726C98.8135 48.7211 99.7453 49.8753 100.485 51.1488L112.442 29.58C112.589 29.3155 112.663 29.0173 112.657 28.7154C112.651 28.4135 112.565 28.1184 112.407 27.86Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFB4AECF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M78.918 47.8292C80.9855 45.9594 83.5807 44.7592 86.3562 44.3892C86.6804 44.3715 87.0053 44.3715 87.3295 44.3892L77.6494 27.86C77.4977 27.6 77.28 27.3839 77.0178 27.233C76.7556 27.0821 76.4581 27.0018 76.1548 27H65.7274C65.4241 27.0018 65.1266 27.0821 64.8644 27.233C64.6022 27.3839 64.3845 27.6 64.2328 27.86C64.0803 28.1215 64 28.4181 64 28.72C64 29.0219 64.0803 29.3185 64.2328 29.58L76.2417 51.1488C76.9635 49.9177 77.8654 48.7991 78.918 47.8292Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFCCCAD8)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _vipPlatinumCongratsSecondary!!
    }

private var _vipPlatinumCongratsSecondary: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun VipPlatinumCongratsSecondaryPreview() {
    Icon(
        imageVector = VipPlatinumCongratsSecondary,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
