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
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

val Delete24: ImageVector
    get() {
        if (_delete24 != null) {
            return _delete24!!
        }
        _delete24 = ImageVector.Builder(
            name = "Delete24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M17.5957,18.6406C17.5246,19.6918 17.4891,20.2177 17.2617,20.6162
                    C17.0616,20.9669 16.7599,21.2488 16.3965,21.4248
                    C15.9836,21.6248 15.4567,21.625 14.4033,21.625L8.97656,21.625
                    C7.92301,21.625 7.39631,21.6247 6.9834,21.4248
                    C6.61993,21.2488 6.31834,20.9669 6.11817,20.6162
                    C5.8908,20.2177 5.85525,19.6918 5.78418,18.6406L4.84473,4.75
                    L18.5352,4.75L17.5957,18.6406Z
                    M11.6152,8.12988C11.3391,8.12992 11.1152,8.35376 11.1152,8.62988
                    L11.1152,17.7549C11.1154,18.0309 11.3392,18.2549 11.6152,18.2549
                    L11.7559,18.2549C12.0319,18.2549 12.2557,18.0309 12.2559,17.7549
                    L12.2559,8.62988C12.2559,8.35374 12.032,8.12988 11.7559,8.12988L11.6152,8.12988Z
                    M8.13379,8.13184C7.85835,8.15089 7.65137,8.38963 7.6709,8.66504
                    L8.31641,17.7676C8.33597,18.043 8.57512,18.2504 8.85059,18.2314L8.99024,18.2227
                    C9.26559,18.2037 9.47341,17.9647 9.4541,17.6895L8.80859,8.58594
                    C8.78892,8.31066 8.54977,8.10313 8.27441,8.12207L8.13379,8.13184Z
                    M15.0947,8.11914C14.8194,8.10015 14.5803,8.30773 14.5605,8.58301
                    L13.915,17.6865C13.8957,17.9617 14.1027,18.2005 14.3779,18.2197L14.5186,18.2295
                    C14.794,18.2485 15.0332,18.0401 15.0527,17.7646L15.6982,8.66211
                    C15.7178,8.38677 15.5107,8.14808 15.2354,8.12891L15.0947,8.11914Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes("M3.98535,5.03125L19.39465,5.03125"),
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFFFFFFFF)),
                strokeLineWidth = .5625F,
                strokeLineCap = StrokeCap.Round,
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M9.26758,3L14.1123,3C14.3884,3 14.6123,3.22386 14.6123,3.5
                    L14.6123,5.375L8.76758,5.375L8.76758,3.5C8.76758,3.22386 8.99144,3 9.26758,3Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFFFFFFFF)),
                strokeLineWidth = 1F,
                strokeLineJoin = StrokeJoin.Round,
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _delete24!!
    }

private var _delete24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Delete24Preview() {
    Icon(
        imageVector = Delete24,
        contentDescription = null,
        tint = Color.Black
    )
}
