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

val Mail22: ImageVector
    get() {
        if (_mail22 != null) {
            return _mail22!!
        }
        _mail22 = ImageVector.Builder(
            name = "Mail22",
            defaultWidth = 22.dp,
            defaultHeight = 22.dp,
            viewportWidth = 22F,
            viewportHeight = 22F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M2.75,6.41659L10.5144,10.7301C10.8164,10.8979 11.1837,10.8979 11.4857,10.7301L19.25,6.41659M19.25,16.4166V5.58325C19.25,5.03097 18.8023,4.58325 18.25,4.58325H3.75C3.19772,4.58325 2.75,5.03097 2.75,5.58325V16.4166C2.75,16.9689 3.19772,17.4166 3.75,17.4166H18.25C18.8023,17.4166 19.25,16.9689 19.25,16.4166Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFF1B1B1B)),
                strokeLineWidth = 1F,
                strokeLineCap = StrokeCap.Round,
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _mail22!!
    }

private var _mail22: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Mail22Preview() {
    Icon(
        imageVector = Mail22,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
