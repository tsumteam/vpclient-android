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

val VipPlatinumWishlist: ImageVector
    get() {
        if (_vipPlatinumWishlist != null) {
            return _vipPlatinumWishlist!!
        }
        _vipPlatinumWishlist = ImageVector.Builder(
            name = "VipPlatinumWishlist",
            defaultWidth = 175.dp,
            defaultHeight = 92.dp,
            viewportWidth = 175F,
            viewportHeight = 92F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M129.984 52C126.444 52.0065 123.774 54.6921 123.774 58.0049C123.774 61.3177 126.444 64.0033 129.738 64.0033C129.821 64.0033 139.927 64 139.927 64L150.879 63.9935C154.167 64.0101 156.83 66.693 156.83 70.0016C156.83 73.3144 154.16 76 150.866 76C150.751 76 123.55 75.9902 123.55 75.9902C122.76 75.9902 122.095 76.3278 121.556 77.003C113.182 86.2175 101.14 92 87.7557 92C62.4958 92 42.0186 71.4051 42.0186 46C41.9651 45.3 41.3017 44.9902 40.0285 44.9902H24.2112C20.5731 45 17.9029 42.3144 17.9029 39.0016C17.9029 35.693 20.5663 33.0101 23.8544 33.0033L38.7829 32.9935L48.7257 33C52.2659 33.0033 54.9362 30.3177 54.9362 27.0049C54.9362 23.818 52.465 21.2116 49.3448 21.0181L38.384 20.9967C34.7459 21.0065 32.0757 18.321 32.0757 15.0082C32.0757 11.6996 34.7392 9.01665 38.0273 9.00984C45.5876 8.95393 52.5529 8.95393 58.9231 9C59.915 9 60.8054 8.7544 61.5944 8.26319C69.0076 3.055 78.027 0 87.7557 0C110.686 0 129.674 16.9707 132.982 39.1049C133.12 39.7016 134.111 40 135.958 40C141.557 39.9595 142.657 42.6996 142.657 46.0082C142.657 49.2262 140.138 51.8523 136.974 52H136.412C136.391 51.999 133.108 51.9967 133.108 51.9967L129.984 52ZM6.96409 45C3.67021 45 1 42.3144 1 39.0016C1 35.6888 3.67021 33.0033 6.96409 33.0033C10.258 33.0033 12.9282 35.6888 12.9282 39.0016C12.9282 42.3144 10.258 45 6.96409 45ZM167.769 76C164.475 76 161.805 73.3144 161.805 70.0016C161.805 66.6888 164.475 64.0033 167.769 64.0033C171.063 64.0033 173.733 66.6888 173.733 70.0016C173.733 73.3144 171.063 76 167.769 76Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFF5F4F9)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M92.6328 20.8938L115.2983 28.3005C116.8732 28.8152,117.737 30.5105,117.2276 32.0871L105.8603 67.2731C105.351 68.8497,103.6614 69.7106,102.0865 69.196L79.421 61.7892C77.8461 61.2745,76.9823 59.5792,77.4916 58.0026L88.8589 22.8166C89.3683 21.24,91.0579 20.3791,92.6328 20.8938Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFE3DEEE)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M86.2243 19.9958L110.0203 21.2501C111.6749 21.3373,112.9463 22.7493,112.86 24.4039L110.9347 61.3531C110.8485 63.0077,109.4373 64.2783,107.7827 64.1911L83.9868 62.9368C82.3322 62.8496,81.0608 61.4376,81.147 59.783L83.0724 22.8338C83.1586 21.1792,84.5698 19.9086,86.2243 19.9958Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFEDECF6)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M68 17L103.76 17C105.4169 17,106.76 18.3431,106.76 20L106.76 72C106.76 73.6569,105.4169 75,103.76 75L68 75C66.3431 75,65 73.6569,65 72L65 20C65 18.3431,66.3431 17,68 17Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M68.1016 17L103.8616 17C105.5185 17,106.8616 18.3431,106.8616 20L106.8616 72C106.8616 73.6569,105.5185 75,103.8616 75L68.1016 75C66.4447 75,65.1016 73.6569,65.1016 72L65.1016 20C65.1016 18.3431,66.4447 17,68.1016 17Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M71.1016 32.6253L73.7193 35L77.1016 31"
                ),
                fill = SolidColor(Color(0x00000000)),
                pathFillType = PathFillType.NonZero,
                stroke = SolidColor(Color(0xFFDFDDE8)),
                strokeLineWidth = 2F,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Miter
            )
            addPath(
                pathData = addPathNodes(
                    "M71.1016 41.6253L73.7193 44L77.1016 40"
                ),
                fill = SolidColor(Color(0x00000000)),
                pathFillType = PathFillType.NonZero,
                stroke = SolidColor(Color(0xFFDFDDE8)),
                strokeLineWidth = 2F,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Miter
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M80.774 50.125C77.3101 50.125 75.1846 53.1541 75.1846 55.6389C75.1846 61.0115 81.2911 65.9982 85.6012 69.1726C89.9115 65.998 96.0179 61.0109 96.0179 55.6389C96.0179 53.1543 93.8923 50.125 90.4285 50.125C88.4941 50.125 86.8414 51.6673 85.6013 53.1325C84.3613 51.6673 82.7084 50.125 80.774 50.125Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFE2E0F0)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M96.0182 55.6389C96.0182 61.0109 89.9118 65.998 85.6016 69.1726L85.6017 53.1325C86.8417 51.6673 88.4944 50.125 90.4288 50.125C93.8926 50.125 96.0182 53.1543 96.0182 55.6389Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFC2BDD3)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M82.1016 32L99.1016 32C99.6539 32,100.1016 32.4477,100.1016 33L100.1016 33C100.1016 33.5523,99.6539 34,99.1016 34L82.1016 34C81.5493 34,81.1016 33.5523,81.1016 33L81.1016 33C81.1016 32.4477,81.5493 32,82.1016 32Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFDFDDE8)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M82.1016 41L99.1016 41C99.6539 41,100.1016 41.4477,100.1016 42L100.1016 42C100.1016 42.5523,99.6539 43,99.1016 43L82.1016 43C81.5493 43,81.1016 42.5523,81.1016 42L81.1016 42C81.1016 41.4477,81.5493 41,82.1016 41Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFDFDDE8)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _vipPlatinumWishlist!!
    }

private var _vipPlatinumWishlist: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun VipPlatinumWishlistPreview() {
    Icon(
        imageVector = VipPlatinumWishlist,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
