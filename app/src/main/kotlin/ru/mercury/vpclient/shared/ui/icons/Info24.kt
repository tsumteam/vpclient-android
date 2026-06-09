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

val Info24: ImageVector
    get() {
        if (_info24 != null) {
            return _info24!!
        }
        _info24 = ImageVector.Builder(
            name = "Info24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(22.5F, 12F)
                arcTo(10.5F, 10.5F, 0F, isMoreThanHalf = true, isPositiveArc = true, x1 = 1.5F, y1 = 12F)
                arcTo(10.5F, 10.5F, 0F, isMoreThanHalf = true, isPositiveArc = true, x1 = 22.5F, y1 = 12F)
            }
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 1.3F,
                strokeLineCap = StrokeCap.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(11.9498F, 9.65F)
                lineTo(11.9498F, 18.35F)
            }
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 1.3F,
                strokeLineCap = StrokeCap.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(11.9498F, 5.65F)
                lineTo(11.9498F, 6.35F)
            }
        }.build()
        return _info24!!
    }

private var _info24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Info24Preview() {
    Icon(
        imageVector = Info24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
