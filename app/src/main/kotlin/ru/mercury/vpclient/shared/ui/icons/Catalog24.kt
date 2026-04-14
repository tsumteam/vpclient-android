package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientTheme

val Catalog24: ImageVector
    get() {
        if (_catalog24 != null) {
            return _catalog24!!
        }
        _catalog24 = ImageVector.Builder(
            name = "Catalog24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                pathFillType = PathFillType.EvenOdd
            ) {
                moveTo(3F, 2.769F)
                curveTo(3F, 1.24F, 4.209F, 0F, 5.7F, 0F)
                horizontalLineTo(20.55F)
                curveTo(20.799F, 0F, 21F, 0.207F, 21F, 0.462F)
                curveTo(21F, 0.716F, 20.799F, 0.923F, 20.55F, 0.923F)
                horizontalLineTo(5.7F)
                curveTo(4.706F, 0.923F, 3.9F, 1.75F, 3.9F, 2.769F)
                verticalLineTo(3.692F)
                curveTo(3.9F, 4.712F, 4.706F, 5.538F, 5.7F, 5.538F)
                horizontalLineTo(21F)
                verticalLineTo(24F)
                horizontalLineTo(5.7F)
                curveTo(4.209F, 24F, 3F, 22.76F, 3F, 21.231F)
                verticalLineTo(2.769F)
                close()
                moveTo(4.8F, 2.308F)
                curveTo(4.8F, 2.053F, 5.001F, 1.846F, 5.25F, 1.846F)
                horizontalLineTo(19.65F)
                curveTo(19.899F, 1.846F, 20.1F, 2.053F, 20.1F, 2.308F)
                curveTo(20.1F, 2.563F, 19.899F, 2.769F, 19.65F, 2.769F)
                horizontalLineTo(5.25F)
                curveTo(5.001F, 2.769F, 4.8F, 2.563F, 4.8F, 2.308F)
                close()
                moveTo(5.25F, 3.692F)
                curveTo(5.001F, 3.692F, 4.8F, 3.899F, 4.8F, 4.154F)
                curveTo(4.8F, 4.409F, 5.001F, 4.615F, 5.25F, 4.615F)
                horizontalLineTo(19.65F)
                curveTo(19.899F, 4.615F, 20.1F, 4.409F, 20.1F, 4.154F)
                curveTo(20.1F, 3.899F, 19.899F, 3.692F, 19.65F, 3.692F)
                horizontalLineTo(5.25F)
                close()
            }
        }.build()
        return _catalog24!!
    }

private var _catalog24: ImageVector? = null

@FontScalePreviews
@Composable
private fun Catalog24Preview() {
    ClientTheme {
        Icon(
            imageVector = Catalog24,
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}
