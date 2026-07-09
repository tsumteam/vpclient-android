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

val VipPlatinumTask: ImageVector
    get() {
        if (_vipPlatinumTask != null) {
            return _vipPlatinumTask!!
        }
        _vipPlatinumTask = ImageVector.Builder(
            name = "VipPlatinumTask",
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
                    M90.5313 18.8938L113.1968 26.3005C114.7717 26.8152,115.6355 28.5105,115.1261 30.0871L103.7588 65.2731C103.2495 66.8497,101.5599 67.7106,99.985 67.196L77.3195 59.7892C75.7446 59.2745,74.8808 57.5792,75.3901 56.0026L86.7574 20.8166C87.2668 19.24,88.9564 18.3791,90.5313 18.8938Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFAFAFC)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M84.1228 17.9958L107.9188 19.2501C109.5734 19.3373,110.8448 20.7493,110.7585 22.4039L108.8332 59.3531C108.747 61.0077,107.3358 62.2783,105.6812 62.1911L81.8853 60.9368C80.2307 60.8496,78.9593 59.4376,79.0455 57.783L80.9709 20.8338C81.0571 19.1792,82.4683 17.9086,84.1228 17.9958Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFDFDFF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M65.8984 15L101.6584 15C103.3153 15,104.6584 16.3431,104.6584 18L104.6584 70C104.6584 71.6569,103.3153 73,101.6584 73L65.8984 73C64.2415 73,62.8984 71.6569,62.8984 70L62.8984 18C62.8984 16.3431,64.2415 15,65.8984 15Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M65.8984 15L101.6584 15C103.3153 15,104.6584 16.3431,104.6584 18L104.6584 70C104.6584 71.6569,103.3153 73,101.6584 73L65.8984 73C64.2415 73,62.8984 71.6569,62.8984 70L62.8984 18C62.8984 16.3431,64.2415 15,65.8984 15Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M81.79 56.5493L96.2072 73.4336H104.589V60.0001L81.79 56.5493Z"
                ),
                fill = SolidColor(Color(0xFFEBEAEF)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    "M69 30.6253L71.6178 33L75 29"
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
                    "M69 39.6253L71.6178 42L75 38"
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
                    "M69 48.6253L71.6178 51L75 47"
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
                    M80 30L97 30C97.5523 30,98 30.4477,98 31L98 31C98 31.5523,97.5523 32,97 32L80 32C79.4477 32,79 31.5523,79 31L79 31C79 30.4477,79.4477 30,80 30Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFDFDDE8)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M80 39L97 39C97.5523 39,98 39.4477,98 40L98 40C98 40.5523,97.5523 41,97 41L80 41C79.4477 41,79 40.5523,79 40L79 40C79 39.4477,79.4477 39,80 39Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFDFDDE8)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M80 48L97 48C97.5523 48,98 48.4477,98 49L98 49C98 49.5523,97.5523 50,97 50L80 50C79.4477 50,79 49.5523,79 49L79 49C79 48.4477,79.4477 48,80 48Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFDFDDE8)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M96.2069 60C103.071 60 108.635 54.4036 108.635 47.5C108.635 40.5964 103.071 35 96.2069 35C89.3428 35 83.7783 40.5964 83.7783 47.5C83.7783 54.4036 89.3428 60 96.2069 60Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFF3F1F8)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M96.2069 60C103.071 60 108.635 54.4036 108.635 47.5C108.635 40.5964 103.071 35 96.2069 35C89.3428 35 83.7783 40.5964 83.7783 47.5C83.7783 54.4036 89.3428 60 96.2069 60Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFF3F1F8)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    "M105.652 39L108.6349 39L108.6349 58L105.652 58Z"
                ),
                fill = SolidColor(Color(0xFFDDDBE7)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M105.652 39L108.6349 39L108.6349 58L105.652 58Z"
                ),
                fill = SolidColor(Color(0xFFF4F2FD)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M95.71 33.5C103.66 33.5 110.119 39.9838 110.119 48C110.119 56.0162 103.66 62.5 95.71 62.5C87.7606 62.4998 81.3018 56.0161 81.3018 48C81.3018 39.9839 87.7606 33.5002 95.71 33.5Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0x00000000)),
                pathFillType = PathFillType.NonZero,
                stroke = SolidColor(Color(0xFF9A96B3)),
                strokeLineWidth = 3F,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M106.23 59.4088C106.79 58.789 107.745 58.748 108.355 59.3175L120.057 70.2411C120.679 70.8212 120.697 71.8044 120.097 72.4074L119.456 73.053C118.866 73.6463 117.907 73.6395 117.326 73.038L106.205 61.5353C105.654 60.9656 105.639 60.0635 106.17 59.4753L106.23 59.4088Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFDAD8E7)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M105.758 60.1758C105.955 59.9776 106.276 59.981 106.468 60.1834L118.75 73.0713C118.937 73.2676 118.933 73.5784 118.742 73.7707C118.545 73.9688 118.225 73.9654 118.032 73.7631L105.751 60.8751C105.564 60.6788 105.567 60.3681 105.758 60.1758Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFB0ABBF)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M102.4286 61.6212L108.7562 55.2573C109.3387 54.6715,110.283 54.6715,110.8654 55.2573L110.8654 55.2573C111.4479 55.843,111.4479 56.7928,110.8654 57.3786L104.5378 63.7426C103.9554 64.3283,103.011 64.3283,102.4286 63.7426L102.4286 63.7426C101.8462 63.1568,101.8462 62.207,102.4286 61.6212Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF9A96B3)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M87.5 38H96.5C97.3284 38 98 38.6716 98 39.5C98 40.3284 97.3284 41 96.5 41H85L86 39.5C86 39.5 86.3047 39.1953 86.5 39C86.8905 38.6095 87.5 38 87.5 38Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFDFDDE8)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M82.8088 47H96.4995C97.3279 47 97.9995 47.6716 97.9995 48.5C97.9995 49.3284 97.3279 50 96.4995 50H83.9995L83.4995 49C83.4995 49 82.8084 48.2761 82.8086 48C82.8089 47.5 82.8088 47 82.8088 47Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFDFDDE8)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M104.1861 61.2678L109.8106 55.6109C110.1989 55.2204,110.8284 55.2204,111.2167 55.6109L111.2167 55.6109C111.605 56.0015,111.605 56.6346,111.2167 57.0252L105.5922 62.682C105.2039 63.0725,104.5744 63.0725,104.1861 62.682L104.1861 62.682C103.7978 62.2915,103.7978 61.6583,104.1861 61.2678Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFB2ADCB)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M105.0955 61.8534L107.2047 59.7321C107.3989 59.5369,107.7137 59.5369,107.9078 59.7321L107.9078 59.7321C108.102 59.9274,108.102 60.244,107.9078 60.4392L105.7986 62.5606C105.6045 62.7558,105.2897 62.7558,105.0955 62.5606L105.0955 62.5606C104.9014 62.3653,104.9014 62.0487,105.0955 61.8534Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFA29DBB)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M105.414 61.2717C105.414 60.8575 105.08 60.5217 104.669 60.5217C104.257 60.5217 103.923 60.8575 103.923 61.2717C103.923 61.6859 104.257 62.0217 104.669 62.0217C105.08 62.0217 105.414 61.6859 105.414 61.2717Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFE4E2ED)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M87.756 54C88.3051 54 88.7503 53.5523 88.7503 53C88.7503 52.4477 88.3051 52 87.756 52C87.2069 52 86.7617 52.4477 86.7617 53C86.7617 53.5523 87.2069 54 87.756 54Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    "M86.6491 50.2619C86.6491 50.2619 85.758 41.902 93.1894 38.9268"
                ),
                fill = SolidColor(Color(0x00000000)),
                pathFillType = PathFillType.NonZero,
                stroke = SolidColor(Color(0xFFFFFFFF)),
                strokeLineWidth = 2F,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter
            )
        }.build()
        return _vipPlatinumTask!!
    }

private var _vipPlatinumTask: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun VipPlatinumTaskPreview() {
    Icon(
        imageVector = VipPlatinumTask,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
