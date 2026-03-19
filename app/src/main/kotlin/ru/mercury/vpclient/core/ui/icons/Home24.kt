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

val Home24: ImageVector
    get() {
        if (_home24 != null) {
            return _home24!!
        }
        _home24 = ImageVector.Builder(
            name = "Home24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(3F, 10.042F)
                curveTo(3F, 9.467F, 3F, 9.18F, 3.074F, 8.915F)
                curveTo(3.14F, 8.681F, 3.248F, 8.46F, 3.393F, 8.264F)
                curveTo(3.556F, 8.043F, 3.784F, 7.867F, 4.238F, 7.515F)
                lineTo(10.066F, 2.991F)
                curveTo(10.77F, 2.444F, 11.123F, 2.17F, 11.511F, 2.066F)
                curveTo(11.854F, 1.973F, 12.216F, 1.974F, 12.558F, 2.068F)
                curveTo(12.946F, 2.174F, 13.298F, 2.449F, 14F, 2.998F)
                lineTo(19.772F, 7.514F)
                curveTo(20.223F, 7.867F, 20.448F, 8.043F, 20.61F, 8.264F)
                curveTo(20.754F, 8.459F, 20.861F, 8.679F, 20.926F, 8.913F)
                curveTo(21F, 9.176F, 21F, 9.462F, 21F, 10.035F)
                verticalLineTo(18.839F)
                curveTo(21F, 19.959F, 21F, 20.52F, 20.782F, 20.947F)
                curveTo(20.59F, 21.324F, 20.284F, 21.63F, 19.908F, 21.821F)
                curveTo(19.48F, 22.039F, 18.92F, 22.039F, 17.8F, 22.039F)
                horizontalLineTo(6.2F)
                curveTo(5.08F, 22.039F, 4.52F, 22.039F, 4.092F, 21.821F)
                curveTo(3.716F, 21.63F, 3.41F, 21.324F, 3.218F, 20.947F)
                curveTo(3F, 20.52F, 3F, 19.959F, 3F, 18.839F)
                lineTo(3F, 10.042F)
                close()
            }
        }.build()
        return _home24!!
    }

private var _home24: ImageVector? = null

@FontScalePreviews
@Composable
private fun Home24Preview() {
    ClientTheme {
        Icon(
            imageVector = Home24,
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}
