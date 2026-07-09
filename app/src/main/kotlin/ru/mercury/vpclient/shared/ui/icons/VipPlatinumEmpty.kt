package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

val VipPlatinumEmpty: ImageVector
    get() {
        if (_vipPlatinumEmpty != null) {
            return _vipPlatinumEmpty!!
        }
        _vipPlatinumEmpty = ImageVector.Builder(
            name = "VipPlatinumEmpty",
            defaultWidth = 210.dp,
            defaultHeight = 111.dp,
            viewportWidth = 210F,
            viewportHeight = 111F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M156.565,62.7391C152.293,62.747 149.07,65.9872 149.07,69.9842C149.07,73.9812 152.293,77.2213 156.268,77.2213C156.368,77.2213 168.565,77.2174 168.565,77.2174L181.783,77.2095C185.751,77.2296 188.965,80.4666 188.965,84.4585C188.965,88.4555 185.743,91.6957 181.767,91.6957C181.628,91.6957 148.799,91.6838 148.799,91.6838C147.846,91.6838 147.044,92.0911 146.393,92.9058C136.287,104.023 121.754,111 105.6,111C75.1136,111 50.3997,86.1518 50.3997,55.5C50.3352,54.6554 49.5345,54.2816 47.9979,54.2816H28.9081C24.5172,54.2935 21.2946,51.0533 21.2946,47.0563C21.2946,43.0644 24.5091,39.8274 28.4775,39.8192L46.4946,39.8073L58.4946,39.8152C62.7672,39.8192 65.9899,36.579 65.9899,32.582C65.9899,28.7369 63.0075,25.5922 59.2417,25.3588L46.0132,25.333C41.6224,25.3449 38.3997,22.1047 38.3997,18.1077C38.3997,14.1158 41.6142,10.8788 45.5826,10.8706C54.7072,10.8031 63.1135,10.8031 70.8017,10.8587C71.9988,10.8587 73.0735,10.5624 74.0258,9.96972C82.9727,3.68592 93.8581,0 105.6,0C133.274,0 156.191,20.4755 160.184,47.1809C160.349,47.9009 161.546,48.2609 163.775,48.2609C170.532,48.212 171.86,51.518 171.86,55.5099C171.86,59.3925 168.819,62.561 165.002,62.7391H164.323C164.297,62.7379 160.335,62.7352 160.335,62.7352L156.565,62.7391ZM8.09257,54.2935C4.1172,54.2935 0.894531,51.0533 0.894531,47.0563C0.894531,43.0594 4.1172,39.8192 8.09257,39.8192C12.0679,39.8192 15.2906,43.0594 15.2906,47.0563C15.2906,51.0533 12.0679,54.2935 8.09257,54.2935ZM202.167,91.6956C198.192,91.6956 194.969,88.4555 194.969,84.4585C194.969,80.4615 198.192,77.2213 202.167,77.2213C206.143,77.2213 209.365,80.4615 209.365,84.4585C209.365,88.4555 206.143,91.6956 202.167,91.6956Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFF5F4F9)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M78.5996,18.0979H123C124.657,18.0979 126,19.4409 126,21.0979V85.0762C126,86.7332 124.657,88.0762 123,88.0762H78.5996C76.9428,88.0762 75.5996,86.7332 75.5996,85.0762V21.0979C75.5996,19.4409 76.9428,18.0979 78.5996,18.0979Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M98.3994,68.2278L115.799,88.599H125.916V72.3912L98.3994,68.2278Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFEBEAEF)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes("M84,50.6738H99.6V53.0868H84V50.6738Z"),
                fill = SolidColor(Color(0xFFDFDDE8)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes("M86.3994,54.2935H98.3994V56.7065H86.3994V54.2935Z"),
                fill = SolidColor(Color(0xFFDFDDE8)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M115.8,72.3913C124.084,72.3913 130.8,65.6391 130.8,57.3098C130.8,48.9805 124.084,42.2283 115.8,42.2283C107.516,42.2283 100.8,48.9805 100.8,57.3098C100.8,65.6391 107.516,72.3913 115.8,72.3913Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFF3F1F8)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M115.2,40.1086C124.968,40.1087 132.9,48.0726 132.9,57.9133C132.9,67.7539 124.968,75.7169 115.2,75.717C105.433,75.717 97.5002,67.7539 97.5,57.9133C97.5,48.0726 105.432,40.1086 115.2,40.1086Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFF9A96B3)),
                strokeLineWidth = 3F,
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M128.107,71.4454C128.667,70.8257 129.622,70.7847 130.232,71.3541L144.82,84.967C145.441,85.547 145.46,86.53 144.86,87.1329L143.637,88.363C143.046,88.9562 142.088,88.9494 141.506,88.348L127.664,74.0342C127.113,73.4647 127.098,72.5627 127.629,71.9746L128.107,71.4454Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFDAD8E7)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M127.326,72.6034C127.564,72.3643 127.951,72.3684 128.184,72.6125L143.006,88.1621C143.231,88.399 143.227,88.7739 142.997,89.0059C142.759,89.245 142.372,89.2409 142.139,88.9967L127.318,73.4471C127.092,73.2103 127.096,72.8354 127.326,72.6034Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFB0ABBF)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes("M102,54.2935H112.8V57.9131H102V54.2935Z"),
                fill = SolidColor(Color(0xFFDFDDE8)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes("M103.199,49.4673H116.399V53.0869H103.199V49.4673Z"),
                fill = SolidColor(Color(0xFFDFDDE8)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M104.265,60.6421C104.265,60.6421 103.189,50.5557 112.158,46.9661
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2F,
                strokeLineCap = StrokeCap.Round,
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _vipPlatinumEmpty!!
    }

private var _vipPlatinumEmpty: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun VipPlatinumEmptyPreview() {
    Icon(
        imageVector = VipPlatinumEmpty,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
