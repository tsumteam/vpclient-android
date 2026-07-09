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

val VipPlatinumCongratsPrimary: ImageVector
    get() {
        if (_vipPlatinumCongratsPrimary != null) {
            return _vipPlatinumCongratsPrimary!!
        }
        _vipPlatinumCongratsPrimary = ImageVector.Builder(
            name = "VipPlatinumCongratsPrimary",
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
                    M91.4423 68.7885L96.2852 79.4768L97.2586 74.8286C97.2898 74.6681 97.3588 74.5174 97.4599 74.389C97.561 74.2606 97.6913 74.1582 97.84 74.0904C97.9887 74.0225 98.1515 73.9911 98.3147 73.9989C98.478 74.0066 98.637 74.0533 98.7786 74.1349L102.95 76.548L98.6019 66.9636C96.3252 67.9417 93.9094 68.5575 91.4423 68.7885Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFB4AECF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M76.3317 77.0831L80.3934 74.4929C80.5308 74.4063 80.6867 74.3535 80.8485 74.3389C81.0102 74.3243 81.1731 74.3482 81.3238 74.4088C81.4744 74.4694 81.6086 74.5649 81.7151 74.6875C81.8217 74.81 81.8977 74.9561 81.9368 75.1137L83.0966 79.7765L87.4987 68.8583C85.0366 68.7191 82.6115 68.196 80.3109 67.308L76.3317 77.0831Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFB4AECF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M88.6513 31.009C85.2513 31.1889 82.0407 32.6304 79.6471 35.0517C77.2535 37.473 75.849 40.7 75.7083 44.1019C75.5676 47.5037 76.7008 50.8357 78.8863 53.4464C81.0718 56.0571 84.1524 57.7588 87.5259 58.2188L87.6178 58.1657C88.6703 58.2569 89.7297 58.2279 90.7755 58.0792C92.2523 57.8736 93.6851 57.4261 95.0163 56.7545C97.7936 55.3615 100.014 53.0643 101.311 50.2411C102.609 47.4179 102.907 44.2371 102.155 41.2222C101.404 38.2073 99.6487 35.5381 97.1781 33.6539C94.7076 31.7697 91.6691 30.7828 88.5628 30.8558L88.6513 31.009Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF9A96B3)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M88.4659 22.5144C83.4161 22.6357 78.5589 24.4763 74.697 27.7322C70.8351 30.988 68.1997 35.4642 67.2265 40.4208C66.2533 45.3773 67.0004 50.5176 69.3444 54.9921C71.6885 59.4665 75.4891 63.0071 80.1181 65.0288C82.9039 66.248 85.9144 66.8694 88.9552 66.8527L89.4073 66.8573L89.7869 66.8425C92.8211 66.7221 95.797 65.9711 98.525 64.6374C103.057 62.4242 106.7 58.7329 108.853 54.1718C111.007 49.6107 111.542 44.4521 110.37 39.5462C109.199 34.6404 106.39 30.2801 102.409 27.184C98.427 24.088 93.5092 22.4409 88.4659 22.5144ZM96.0143 58.7319C94.4831 59.5058 92.8349 60.0225 91.1359 60.2613C90.4967 60.3635 89.8522 60.4287 89.2055 60.4564C88.5767 60.4668 87.9479 60.441 87.3221 60.3792L87.1706 60.3646C83.4163 59.957 79.9344 58.2109 77.3623 55.446C74.7901 52.681 73.2999 49.0821 73.1644 45.3082C73.0289 41.5343 74.2572 37.8378 76.6244 34.8955C78.9916 31.9531 82.3393 29.9618 86.0547 29.2861C89.7701 28.6104 93.6046 29.2954 96.8562 31.2158C100.108 33.1362 102.559 36.1635 103.761 39.7435C104.963 43.3234 104.835 47.2166 103.401 50.7101C101.968 54.2037 99.3234 57.0639 95.953 58.7673L96.0143 58.7319Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFCCCAD8)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _vipPlatinumCongratsPrimary!!
    }

private var _vipPlatinumCongratsPrimary: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun VipPlatinumCongratsPrimaryPreview() {
    Icon(
        imageVector = VipPlatinumCongratsPrimary,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
