package ru.mercury.vpclient.core.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientTheme

val Brands24: ImageVector
    get() {
        if (_brands24 != null) {
            return _brands24!!
        }
        _brands24 = ImageVector.Builder(
            name = "Brands24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(0.984F, 11.641F)
                curveTo(0.611F, 11.267F, 0.4F, 10.761F, 0.398F, 10.233F)
                lineTo(0.372F, 2.201F)
                curveTo(0.368F, 1.091F, 1.269F, 0.191F, 2.378F, 0.194F)
                lineTo(10.41F, 0.221F)
                curveTo(10.938F, 0.223F, 11.444F, 0.433F, 11.818F, 0.807F)
                lineTo(23.478F, 12.467F)
                curveTo(23.868F, 12.857F, 23.868F, 13.49F, 23.478F, 13.881F)
                lineTo(14.058F, 23.3F)
                curveTo(13.668F, 23.691F, 13.035F, 23.691F, 12.644F, 23.3F)
                lineTo(0.984F, 11.641F)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(11.134F, 4.847F)
                lineTo(17.215F, 10.928F)
                arcTo(0.482F, 0.482F, 0F, false, true, 17.215F, 11.609F)
                lineTo(17.215F, 11.609F)
                arcTo(0.482F, 0.482F, 90F, false, true, 16.533F, 11.609F)
                lineTo(10.452F, 5.529F)
                arcTo(0.482F, 0.482F, 0F, false, true, 10.452F, 4.847F)
                lineTo(10.452F, 4.847F)
                arcTo(0.482F, 0.482F, 90F, false, true, 11.134F, 4.847F)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(8.404F, 7.633F)
                lineTo(15.848F, 15.076F)
                arcTo(0.482F, 0.482F, 90F, false, true, 15.848F, 15.758F)
                lineTo(15.848F, 15.758F)
                arcTo(0.482F, 0.482F, 90F, false, true, 15.166F, 15.758F)
                lineTo(7.722F, 8.314F)
                arcTo(0.482F, 0.482F, 90F, false, true, 7.722F, 7.633F)
                lineTo(7.722F, 7.633F)
                arcTo(0.482F, 0.482F, 90F, false, true, 8.404F, 7.633F)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(5.692F, 10.347F)
                lineTo(10.41F, 15.065F)
                arcTo(0.482F, 0.482F, 90F, false, true, 10.41F, 15.746F)
                lineTo(10.41F, 15.746F)
                arcTo(0.482F, 0.482F, 0F, false, true, 9.728F, 15.746F)
                lineTo(5.011F, 11.029F)
                arcTo(0.482F, 0.482F, 0F, false, true, 5.011F, 10.347F)
                lineTo(5.011F, 10.347F)
                arcTo(0.482F, 0.482F, 90F, false, true, 5.692F, 10.347F)
                close()
            }
        }.build()
        return _brands24!!
    }

private var _brands24: ImageVector? = null

@FontScalePreviews
@Composable
private fun Brands24Preview() {
    ClientTheme {
        Icon(
            imageVector = Brands24,
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}
