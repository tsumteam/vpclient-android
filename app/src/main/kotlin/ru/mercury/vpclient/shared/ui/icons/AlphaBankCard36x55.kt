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

val AlphaBankCard36x55: ImageVector
    get() {
        if (_alphaBankCard36x55 != null) {
            return _alphaBankCard36x55!!
        }
        _alphaBankCard36x55 = ImageVector.Builder(
            name = "AlphaBankCard36x55",
            defaultWidth = 36.dp,
            defaultHeight = 55.dp,
            viewportWidth = 36F,
            viewportHeight = 55F
        ).apply {
            path(
                fill = SolidColor(Color(0xFFEF3123)),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(2.78F, 0F)
                horizontalLineTo(33.22F)
                arcTo(
                    2.78F,
                    2.78F,
                    0F,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    x1 = 36F,
                    y1 = 2.78F
                )
                verticalLineTo(52.22F)
                arcTo(
                    2.78F,
                    2.78F,
                    0F,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    x1 = 33.22F,
                    y1 = 55F
                )
                horizontalLineTo(2.78F)
                arcTo(
                    2.78F,
                    2.78F,
                    0F,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    x1 = 0F,
                    y1 = 52.22F
                )
                verticalLineTo(2.78F)
                arcTo(
                    2.78F,
                    2.78F,
                    0F,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    x1 = 2.78F,
                    y1 = 0F
                )
                close()
            }
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(25.32F, 35.44F)
                horizontalLineTo(10.97F)
                verticalLineTo(38.55F)
                horizontalLineTo(25.32F)
                verticalLineTo(35.44F)
                close()
            }
            path(
                fill = SolidColor(Color.White),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20.77F, 18.08F)
                curveTo(20.36F, 16.81F, 19.89F, 15.8F, 18.27F, 15.8F)
                curveTo(16.66F, 15.8F, 16.16F, 16.8F, 15.73F, 18.08F)
                lineTo(11.29F, 31.22F)
                horizontalLineTo(14.23F)
                lineTo(15.25F, 28.1F)
                horizontalLineTo(20.92F)
                lineTo(21.87F, 31.22F)
                horizontalLineTo(25F)
                lineTo(20.77F, 18.08F)
                close()
                moveTo(16.11F, 25.46F)
                lineTo(18.13F, 19.23F)
                horizontalLineTo(18.2F)
                lineTo(20.1F, 25.46F)
                horizontalLineTo(16.11F)
                close()
            }
        }.build()
        return _alphaBankCard36x55!!
    }

private var _alphaBankCard36x55: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun AlphaBankCard36x55Preview() {
    Icon(
        imageVector = AlphaBankCard36x55,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
