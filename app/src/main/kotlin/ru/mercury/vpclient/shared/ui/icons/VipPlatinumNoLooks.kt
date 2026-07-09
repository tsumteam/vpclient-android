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

val VipPlatinumNoLooks: ImageVector
    get() {
        if (_vipPlatinumNoLooks != null) {
            return _vipPlatinumNoLooks!!
        }
        _vipPlatinumNoLooks = ImageVector.Builder(
            name = "VipPlatinumNoLooks",
            defaultWidth = 174.dp,
            defaultHeight = 92.dp,
            viewportWidth = 174F,
            viewportHeight = 92F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M129.726 52C126.185 52.0065 123.515 54.6921 123.515 58.0049C123.515 61.3177 126.185 64.0033 129.479 64.0033C129.562 64.0033 139.669 64 139.669 64L150.62 63.9935C153.908 64.0101 156.571 66.693 156.571 70.0016C156.571 73.3144 153.901 76 150.607 76C150.492 76 123.291 75.9902 123.291 75.9902C122.501 75.9902 121.837 76.3278 121.297 77.003C112.924 86.2175 100.882 92 87.4969 92C62.237 92 41.7598 71.4051 41.7598 46C41.7063 45.3 41.0429 44.9902 39.7697 44.9902H23.9524C20.3143 45 17.6441 42.3144 17.6441 39.0016C17.6441 35.693 20.3075 33.0101 23.5956 33.0033L38.5241 32.9935L48.4669 33C52.0072 33.0033 54.6774 30.3177 54.6774 27.0049C54.6774 23.818 52.2063 21.2116 49.086 21.0181L38.1253 20.9967C34.4871 21.0065 31.8169 18.321 31.8169 15.0082C31.8169 11.6996 34.4804 9.01665 37.7685 9.00984C45.3288 8.95393 52.2941 8.95393 58.6643 9C59.6562 9 60.5466 8.7544 61.3357 8.26319C68.7489 3.055 77.7682 0 87.4969 0C110.427 0 129.416 16.9707 132.724 39.1049C132.861 39.7016 133.853 40 135.699 40C141.298 39.9595 142.399 42.6996 142.399 46.0082C142.399 49.2262 139.879 51.8523 136.716 52H136.153C136.132 51.999 132.849 51.9967 132.849 51.9967L129.726 52ZM6.7053 45C3.41142 45 0.741211 42.3144 0.741211 39.0016C0.741211 35.6888 3.41142 33.0033 6.7053 33.0033C9.99917 33.0033 12.6694 35.6888 12.6694 39.0016C12.6694 42.3144 9.99917 45 6.7053 45ZM167.51 76C164.216 76 161.546 73.3144 161.546 70.0016C161.546 66.6888 164.216 64.0033 167.51 64.0033C170.804 64.0033 173.474 66.6888 173.474 70.0016C173.474 73.3144 170.804 76 167.51 76Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFF5F4F9)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M90.2725 18.8938L112.938 26.3005C114.5129 26.8152,115.3767 28.5105,114.8673 30.0871L103.5 65.2731C102.9907 66.8497,101.3011 67.7106,99.7262 67.196L77.0607 59.7892C75.4858 59.2745,74.622 57.5792,75.1313 56.0026L86.4986 20.8166C87.008 19.24,88.6976 18.3791,90.2725 18.8938Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFAFAFC)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M83.864 17.9958L107.66 19.2501C109.3146 19.3373,110.586 20.7493,110.4997 22.4039L108.5744 59.3531C108.4882 61.0077,107.077 62.2783,105.4224 62.1911L81.6265 60.9368C79.9719 60.8496,78.7005 59.4376,78.7867 57.783L80.7121 20.8338C80.7983 19.1792,82.2095 17.9086,83.864 17.9958Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFDFDFF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M65.6396 15L101.3996 15C103.0565 15,104.3996 16.3431,104.3996 18L104.3996 70C104.3996 71.6569,103.0565 73,101.3996 73L65.6396 73C63.9827 73,62.6396 71.6569,62.6396 70L62.6396 18C62.6396 16.3431,63.9827 15,65.6396 15Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M65.6396 15L101.3996 15C103.0565 15,104.3996 16.3431,104.3996 18L104.3996 70C104.3996 71.6569,103.0565 73,101.3996 73L65.6396 73C63.9827 73,62.6396 71.6569,62.6396 70L62.6396 18C62.6396 16.3431,63.9827 15,65.6396 15Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M82 56.0004L95.9484 73.4335H104.33V60L82 56.0004Z"
                ),
                fill = SolidColor(Color(0xFFEBEAEF)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M95.9481 60C102.812 60 108.377 54.4036 108.377 47.5C108.377 40.5964 102.812 35 95.9481 35C89.084 35 83.5195 40.5964 83.5195 47.5C83.5195 54.4036 89.084 60 95.9481 60Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFF3F1F8)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M95.9481 60C102.812 60 108.377 54.4036 108.377 47.5C108.377 40.5964 102.812 35 95.9481 35C89.084 35 83.5195 40.5964 83.5195 47.5C83.5195 54.4036 89.084 60 95.9481 60Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFF3F1F8)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    "M105.394 39L108.3769 39L108.3769 58L105.394 58Z"
                ),
                fill = SolidColor(Color(0xFFDDDBE7)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M105.394 39L108.3769 39L108.3769 58L105.394 58Z"
                ),
                fill = SolidColor(Color(0xFFF4F2FD)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M83.5045 25C85.4569 25.0001 87.0483 26.5894 87.0407 28.5391C87.0405 30.1048 86.0104 31.4408 84.5885 31.9014V33.2139L87.4635 35.1553C87.6327 34.7794 88.009 34.5186 88.4469 34.5186C92.0906 34.5186 95.5272 36.1079 97.8717 38.8867L101.746 43.4688C102.107 43.8986 102.084 44.5364 101.669 44.9355L97.0875 49.5098C96.8646 49.7324 96.557 49.8482 96.2418 49.8252C95.9267 49.8022 95.6343 49.6409 95.4498 49.3877L94.7428 48.4355V62.9053C94.7428 63.504 94.2584 63.9873 93.6588 63.9873H73.3268C72.7272 63.9873 72.2428 63.504 72.2428 62.9053V48.4277L71.5358 49.3799C71.3436 49.6332 71.0589 49.7943 70.7438 49.8174C70.4287 49.8404 70.121 49.7254 69.8981 49.5029L65.317 44.9277C64.9173 44.5286 64.8942 43.8908 65.2555 43.4609L69.1295 38.8789C71.4817 36.1002 74.9175 34.5109 78.5534 34.5107C78.9915 34.5107 79.3686 34.7723 79.5377 35.1484L82.4127 33.2139V30.9873C82.4128 30.3886 82.8972 29.9053 83.4967 29.9053C84.2499 29.9052 84.8647 29.2911 84.8649 28.5391C84.8649 27.7868 84.25 27.1729 83.4967 27.1729C82.7434 27.1729 82.1285 27.7868 82.1285 28.5391C82.1284 29.1376 81.644 29.621 81.0446 29.6211C80.4451 29.6211 79.9608 29.1377 79.9606 28.5391C79.9606 26.5893 81.552 25 83.5045 25ZM89.4078 36.7295C88.8774 39.5082 86.4253 41.6191 83.4889 41.6191C80.5525 41.6191 78.1004 39.5083 77.57 36.7295C74.941 36.9828 72.5038 38.2491 70.7819 40.2832L67.5534 44.1064L70.5436 47.085L72.4577 44.5137C72.7421 44.1375 73.2266 43.9831 73.6725 44.1289C74.1183 44.2748 74.4176 44.69 74.4176 45.1582V61.8311H92.5827V45.1582C92.5827 44.69 92.8821 44.2748 93.3278 44.1289C93.7736 43.9831 94.2659 44.1375 94.5426 44.5137L96.4567 47.085L99.4313 44.1064L96.1959 40.2832C94.474 38.249 92.0368 36.9828 89.4078 36.7295ZM80.0914 37.3887C80.7372 38.6092 82.0286 39.4463 83.5045 39.4463C84.9803 39.4462 86.2719 38.6091 86.9176 37.3887L83.5045 35.0938L80.0914 37.3887Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFDFDDE8)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M95.4512 33.5C103.401 33.5 109.86 39.9838 109.86 48C109.86 56.0162 103.401 62.5 95.4512 62.5C87.5018 62.4998 81.043 56.0161 81.043 48C81.043 39.9839 87.5018 33.5002 95.4512 33.5Z
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
                    M105.971 59.4088C106.531 58.789 107.486 58.748 108.096 59.3175L119.799 70.2411C120.42 70.8212 120.438 71.8044 119.839 72.4074L119.197 73.053C118.607 73.6463 117.648 73.6395 117.067 73.038L105.946 61.5353C105.396 60.9656 105.38 60.0635 105.911 59.4753L105.971 59.4088Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFDAD8E7)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M105.499 60.1758C105.696 59.9776 106.017 59.981 106.21 60.1834L118.491 73.0713C118.678 73.2676 118.675 73.5784 118.483 73.7707C118.286 73.9688 117.966 73.9654 117.773 73.7631L105.492 60.8751C105.305 60.6788 105.308 60.3681 105.499 60.1758Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFB0ABBF)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M102.1696 61.6213L108.4972 55.2574C109.0797 54.6716,110.024 54.6716,110.6064 55.2574L110.6064 55.2574C111.1889 55.8431,111.1889 56.7929,110.6064 57.3787L104.2788 63.7427C103.6964 64.3284,102.752 64.3284,102.1696 63.7427L102.1696 63.7427C101.5872 63.1569,101.5872 62.2071,102.1696 61.6213Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF9A96B3)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M103.9281 61.2678L109.5526 55.6109C109.9409 55.2204,110.5704 55.2204,110.9587 55.6109L110.9587 55.6109C111.347 56.0015,111.347 56.6346,110.9587 57.0252L105.3342 62.682C104.9459 63.0725,104.3164 63.0725,103.9281 62.682L103.9281 62.682C103.5398 62.2915,103.5398 61.6583,103.9281 61.2678Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFB2ADCB)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M104.8365 61.8536L106.9457 59.7323C107.1399 59.5371,107.4547 59.5371,107.6488 59.7323L107.6488 59.7323C107.843 59.9276,107.843 60.2442,107.6488 60.4394L105.5396 62.5608C105.3455 62.756,105.0307 62.756,104.8365 62.5608L104.8365 62.5608C104.6424 62.3655,104.6424 62.0489,104.8365 61.8536Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFA29DBB)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M105.155 61.2718C105.155 60.8576 104.822 60.5218 104.41 60.5218C103.998 60.5218 103.664 60.8576 103.664 61.2718C103.664 61.6861 103.998 62.0219 104.41 62.0219C104.822 62.0219 105.155 61.6861 105.155 61.2718Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFE4E2ED)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M87.4972 54C88.0463 54 88.4915 53.5523 88.4915 53C88.4915 52.4477 88.0463 52 87.4972 52C86.9481 52 86.5029 52.4477 86.5029 53C86.5029 53.5523 86.9481 54 87.4972 54Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    "M86.3903 50.262C86.3903 50.262 85.4993 41.9021 92.9306 38.9269"
                ),
                fill = SolidColor(Color(0x00000000)),
                pathFillType = PathFillType.NonZero,
                stroke = SolidColor(Color(0xFFFFFFFF)),
                strokeLineWidth = 2F,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter
            )
        }.build()
        return _vipPlatinumNoLooks!!
    }

private var _vipPlatinumNoLooks: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun VipPlatinumNoLooksPreview() {
    Icon(
        imageVector = VipPlatinumNoLooks,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
