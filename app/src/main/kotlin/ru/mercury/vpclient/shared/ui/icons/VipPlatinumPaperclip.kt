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

val VipPlatinumPaperclip: ImageVector
    get() {
        if (_vipPlatinumPaperclip != null) {
            return _vipPlatinumPaperclip!!
        }
        _vipPlatinumPaperclip = ImageVector.Builder(
            name = "VipPlatinumPaperclip",
            defaultWidth = 174.dp,
            defaultHeight = 92.dp,
            viewportWidth = 174F,
            viewportHeight = 92F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M6.57393 32.8284C3.21506 32.8284 0.492188 35.5696 0.492188 38.9488C0.492188 42.3291 3.21506 45.0703 6.57393 45.0703C9.9328 45.0703 12.6557 42.3291 12.6557 38.9488C12.6557 35.5696 9.9328 32.8284 6.57393 32.8284Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFF5F4F9)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M166.595 63.4331C163.237 63.4331 160.514 66.1743 160.514 69.5536C160.514 72.9338 163.237 75.675 166.595 75.675C169.954 75.675 172.677 72.9338 172.677 69.5536C172.677 66.1743 169.954 63.4331 166.595 63.4331Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFF5F4F9)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M149.638 63.433H142.116H126.341H125.589C122.23 63.433 119.507 60.6918 119.507 57.3125C119.507 53.9323 122.23 51.1911 125.589 51.1911H129.524H136.033C139.392 51.1911 142.116 48.4499 142.116 45.0706C142.116 41.6904 139.392 38.9492 136.033 38.9492H129.32C126.005 17.1738 107.312 0.494141 84.7475 0.494141C75.3472 0.494141 66.6212 3.39264 59.3962 8.34382H37.1355C33.7766 8.34382 31.0538 11.085 31.0538 14.4653C31.0538 17.8455 33.7766 20.5858 37.1355 20.5858H47.3016H47.5797C50.9396 20.5858 53.6625 23.327 53.6625 26.7072C53.6625 30.0875 50.9396 32.8287 47.5797 32.8287H41.554H31.0538H23.53C20.1711 32.8287 17.4482 35.5689 17.4482 38.9492C17.4482 42.3294 20.1711 45.0706 23.53 45.0706H39.6736C39.6687 45.3397 39.6529 45.6058 39.6529 45.8759C39.6529 70.9404 59.8425 91.2576 84.7475 91.2576C98.3166 91.2576 110.476 85.2182 118.743 75.675H149.638C152.997 75.675 155.72 72.9338 155.72 69.5535C155.72 66.1742 152.997 63.433 149.638 63.433Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFF5F4F9)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M73.001 66L100.001 66C100.5533 66,101.001 66.4477,101.001 67L101.001 67C101.001 67.5523,100.5533 68,100.001 68L73.001 68C72.4487 68,72.001 67.5523,72.001 67L72.001 67C72.001 66.4477,72.4487 66,73.001 66Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFB4AECF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M98.8961 30.9139C95.0108 27.0287 88.6897 27.0287 84.8045 30.9139L69.9677 45.7506C69.5098 46.2085 69.5098 46.9503 69.9677 47.4082C70.4256 47.8661 71.1674 47.8661 71.6253 47.4082L86.462 32.5715C89.3421 29.6937 94.3583 29.6937 97.2384 32.5715C100.209 35.5421 100.209 40.3763 97.2384 43.3468L76.5209 64.2976C74.7489 66.0696 71.6615 66.0696 69.8896 64.2976C68.0614 62.4695 68.0614 59.4955 69.8896 57.6674L89.7783 37.5442C90.4422 36.8803 91.6007 36.8803 92.2646 37.5442C92.9503 38.2298 92.9503 39.346 92.2646 40.0317L73.2058 59.3249C72.8105 59.7202 72.8105 60.587 73.2058 60.9824C73.6637 61.4403 74.4055 61.4403 74.8634 60.9824L93.9221 41.6892C95.5225 40.0888 95.5225 37.4869 93.9221 35.8866C92.3722 34.3367 89.6683 34.3389 88.1207 35.8866L68.232 56.0097C65.5112 58.7305 65.5112 63.2342 68.232 65.9551C69.5611 67.2842 71.3273 68 73.2057 68C75.0842 68 76.8506 67.2842 78.1785 65.9552L98.896 45.0043C102.781 41.1191 102.781 34.7991 98.8961 30.9139Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFB4AECF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M97.8961 29.9139C94.0108 26.0287 87.6897 26.0287 83.8045 29.9139L68.9677 44.7506C68.5098 45.2085 68.5098 45.9503 68.9677 46.4082C69.4256 46.8661 70.1674 46.8661 70.6253 46.4082L85.462 31.5715C88.3421 28.6937 93.3583 28.6937 96.2384 31.5715C99.2089 34.5421 99.2089 39.3763 96.2384 42.3468L75.5209 63.2976C73.7489 65.0696 70.6615 65.0696 68.8896 63.2976C67.0614 61.4695 67.0614 58.4955 68.8896 56.6674L88.7783 36.5442C89.4422 35.8803 90.6007 35.8803 91.2646 36.5442C91.9503 37.2298 91.9503 38.346 91.2646 39.0317L72.2058 58.3249C71.8105 58.7202 71.8105 59.587 72.2058 59.9824C72.6637 60.4403 73.4055 60.4403 73.8634 59.9824L92.9221 40.6892C94.5225 39.0888 94.5225 36.4869 92.9221 34.8866C91.3722 33.3367 88.6683 33.3389 87.1207 34.8866L67.232 55.0097C64.5112 57.7305 64.5112 62.2342 67.232 64.9551C68.5611 66.2842 70.3273 67 72.2057 67C74.0842 67 75.8506 66.2842 77.1785 64.9552L97.896 44.0043C101.781 40.1191 101.781 33.7991 97.8961 29.9139Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _vipPlatinumPaperclip!!
    }

private var _vipPlatinumPaperclip: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun VipPlatinumPaperclipPreview() {
    Icon(
        imageVector = VipPlatinumPaperclip,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
