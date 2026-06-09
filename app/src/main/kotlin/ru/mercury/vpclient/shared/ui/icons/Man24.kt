package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

val Man24: ImageVector
    get() {
        if (_man24 != null) {
            return _man24!!
        }
        _man24 = ImageVector.Builder(
            name = "Man24",
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
                moveTo(16.5F, 6F)
                arcTo(4.5F, 4.5F, 0F, isMoreThanHalf = true, isPositiveArc = true, x1 = 7.5F, y1 = 6F)
                arcTo(4.5F, 4.5F, 0F, isMoreThanHalf = true, isPositiveArc = true, x1 = 16.5F, y1 = 6F)
            }
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(12F, 13.5F)
                curveTo(16.5266F, 13.5F, 20.2268F, 17.0385F, 20.4854F, 21.5F)
                horizontalLineTo(3.51465F)
                curveTo(3.77322F, 17.0385F, 7.47345F, 13.5F, 12F, 13.5F)
                close()
            }
        }.build()
        return _man24!!
    }

private var _man24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Man24Preview() {
    Icon(
        imageVector = Man24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
