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

val Consultants24: ImageVector
    get() {
        if (_consultants24 != null) {
            return _consultants24!!
        }
        _consultants24 = ImageVector.Builder(
            name = "Consultants24",
            defaultWidth = 26.dp,
            defaultHeight = 26.dp,
            viewportWidth = 26F,
            viewportHeight = 26F
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(7.493F, 17.679F)
                curveTo(8.109F, 17.83F, 8.754F, 18.131F, 9.459F, 18.728F)
                curveTo(10.01F, 19.195F, 11.343F, 20.04F, 11.343F, 21.636F)
                curveTo(11.342F, 24.218F, 9.944F, 24.222F, 9.932F, 24.222F)
                horizontalLineTo(2.446F)
                curveTo(1.774F, 24.222F, 1.093F, 24.028F, 0.682F, 23.496F)
                curveTo(0.337F, 23.05F, 0F, 22.415F, 0F, 21.636F)
                curveTo(0F, 20.024F, 1.346F, 19.197F, 1.899F, 18.728F)
                curveTo(2.598F, 18.137F, 3.24F, 17.837F, 3.853F, 17.684F)
                curveTo(4.267F, 18.03F, 4.897F, 18.411F, 5.666F, 18.411F)
                curveTo(6.439F, 18.411F, 7.075F, 18.027F, 7.493F, 17.679F)
                close()
                moveTo(5.66F, 8.036F)
                curveTo(7.883F, 8.036F, 8.721F, 9.442F, 8.721F, 12.235F)
                curveTo(8.721F, 15.028F, 7.436F, 17.391F, 5.66F, 17.391F)
                curveTo(3.884F, 17.391F, 2.623F, 14.738F, 2.623F, 12.235F)
                curveTo(2.623F, 9.732F, 3.437F, 8.036F, 5.66F, 8.036F)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)),
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(18.065F, 14.629F)
                curveTo(19.095F, 14.871F, 20.172F, 15.352F, 21.351F, 16.31F)
                curveTo(22.271F, 17.059F, 24.498F, 18.414F, 24.498F, 20.974F)
                curveTo(24.498F, 25.123F, 22.149F, 25.12F, 22.139F, 25.12F)
                horizontalLineTo(8.904F)
                curveTo(8.253F, 25.12F, 7.607F, 24.932F, 7.147F, 24.471F)
                curveTo(6.458F, 23.778F, 5.546F, 22.562F, 5.546F, 20.974F)
                curveTo(5.546F, 18.387F, 7.795F, 17.063F, 8.72F, 16.31F)
                curveTo(9.885F, 15.363F, 10.957F, 14.881F, 11.98F, 14.637F)
                curveTo(12.673F, 15.191F, 13.726F, 15.802F, 15.013F, 15.802F)
                curveTo(16.305F, 15.802F, 17.368F, 15.186F, 18.065F, 14.629F)
                close()
                moveTo(15.001F, 0.75F)
                curveTo(19.057F, 0.75F, 20.585F, 2.94F, 20.585F, 7.29F)
                curveTo(20.585F, 11.64F, 18.241F, 15.32F, 15.001F, 15.32F)
                curveTo(11.761F, 15.32F, 9.46F, 11.189F, 9.46F, 7.29F)
                curveTo(9.46F, 3.392F, 10.945F, 0.75F, 15.001F, 0.75F)
                close()
            }
        }.build()
        return _consultants24!!
    }

private var _consultants24: ImageVector? = null

@FontScalePreviews
@Composable
private fun Consultants24Preview() {
    ClientTheme {
        Icon(
            imageVector = Consultants24,
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}
