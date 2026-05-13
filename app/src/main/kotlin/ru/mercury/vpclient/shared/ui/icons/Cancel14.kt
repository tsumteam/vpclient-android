package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

val Cancel14: ImageVector
    get() {
        if (_cancel14 != null) {
            return _cancel14!!
        }
        _cancel14 = ImageVector.Builder(
            name = "Cancel14",
            defaultWidth = 14.dp,
            defaultHeight = 14.dp,
            viewportWidth = 14F,
            viewportHeight = 14F
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                stroke = SolidColor(Color(0xFFFFFFFF)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(11.083F, 11.0834F)
                lineTo(2.9163F, 2.9168F)
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                stroke = SolidColor(Color(0xFFFFFFFF)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(11.083F, 2.9168F)
                lineTo(2.9163F, 11.0834F)
            }
        }.build()
        return _cancel14!!
    }

private var _cancel14: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Cancel14Preview() {
    Icon(
        imageVector = Cancel14,
        contentDescription = null,
        tint = Color.Black
    )
}
