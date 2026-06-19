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
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

val CloseSmall24: ImageVector
    get() {
        if (_closeSmall24 != null) {
            return _closeSmall24!!
        }
        _closeSmall24 = ImageVector.Builder(
            name = "CloseSmall24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(15.7334F, 8.2666F)
                lineTo(8.2667F, 15.7333F)
            }
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(8.2667F, 8.2666F)
                lineTo(15.7334F, 15.7333F)
            }
        }.build()
        return _closeSmall24!!
    }

private var _closeSmall24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CloseSmall24Preview() {
    Icon(
        imageVector = CloseSmall24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
