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

val Profile24: ImageVector
    get() {
        if (_profile24 != null) {
            return _profile24!!
        }
        _profile24 = ImageVector.Builder(
            name = "Profile24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(15.69F, 14.316F)
                curveTo(16.941F, 14.539F, 18.25F, 14.984F, 19.681F, 15.869F)
                curveTo(20.798F, 16.56F, 23.5F, 17.81F, 23.5F, 20.173F)
                curveTo(23.5F, 24.009F, 20.64F, 24.001F, 20.638F, 24.001F)
                horizontalLineTo(4.329F)
                curveTo(3.694F, 24.001F, 3.062F, 23.858F, 2.551F, 23.481F)
                curveTo(1.704F, 22.858F, 0.5F, 21.702F, 0.5F, 20.173F)
                curveTo(0.5F, 17.785F, 3.231F, 16.563F, 4.353F, 15.869F)
                curveTo(5.769F, 14.993F, 7.069F, 14.549F, 8.313F, 14.323F)
                curveTo(9.154F, 14.835F, 10.431F, 15.398F, 11.989F, 15.398F)
                curveTo(13.555F, 15.397F, 14.844F, 14.83F, 15.69F, 14.316F)
                close()
                moveTo(12F, 0.042F)
                curveTo(15.976F, 0.042F, 17.473F, 2.123F, 17.473F, 6.257F)
                curveTo(17.473F, 10.391F, 15.176F, 13.889F, 12F, 13.889F)
                curveTo(8.824F, 13.889F, 6.568F, 9.962F, 6.568F, 6.257F)
                curveTo(6.568F, 2.552F, 8.024F, 0.042F, 12F, 0.042F)
                close()
            }
        }.build()
        return _profile24!!
    }

private var _profile24: ImageVector? = null

@FontScalePreviews
@Composable
private fun Profile24Preview() {
    ClientTheme {
        Icon(
            imageVector = Profile24,
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}
