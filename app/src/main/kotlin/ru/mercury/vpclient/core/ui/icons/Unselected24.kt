package ru.mercury.vpclient.core.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientTheme

val Unselected24: ImageVector
    get() {
        if (_unselected24 != null) {
            return _unselected24!!
        }
        _unselected24 = ImageVector.Builder(
            name = "Unselected24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes("M12,2.5A9.5,9.5 0,1 1,12 21.5A9.5,9.5 0,1 1,12 2.5Z"),
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _unselected24!!
    }

private var _unselected24: ImageVector? = null

@FontScalePreviews
@Composable
private fun Unselected24Preview() {
    ClientTheme {
        Icon(
            imageVector = Unselected24,
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}
