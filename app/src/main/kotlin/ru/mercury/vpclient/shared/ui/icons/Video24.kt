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
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

val Video24: ImageVector
    get() {
        if (_video24 != null) {
            return _video24!!
        }
        _video24 = ImageVector.Builder(
            name = "Video24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            path(
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(22.5F, 12F)
                arcTo(10.5F, 10.5F, 0F,
                    isMoreThanHalf = true,
                    isPositiveArc = true,
                    x1 = 1.5F,
                    y1 = 12F
                )
                arcTo(10.5F, 10.5F, 0F,
                    isMoreThanHalf = true,
                    isPositiveArc = true,
                    x1 = 22.5F,
                    y1 = 12F
                )
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                stroke = SolidColor(Color(0xFF000000)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(10.25F, 15.0312F)
                lineTo(10.25F, 8.96875F)
                lineTo(15.501F, 12F)
                lineTo(10.25F, 15.0312F)
                close()
            }
        }.build()
        return _video24!!
    }

private var _video24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Video24Preview() {
    Icon(
        imageVector = Video24,
        contentDescription = null
    )
}
