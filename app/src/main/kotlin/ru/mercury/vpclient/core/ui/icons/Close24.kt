package ru.mercury.vpclient.core.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.theme.ClientTheme

val Close24: ImageVector
    get() {
        if (_close24 != null) {
            return _close24!!
        }
        _close24 = ImageVector.Builder(
            name = "Close24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                stroke = SolidColor(Color(0xFFFFFFFF)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(19F, 5F)
                lineTo(5F, 19F)
            }
            path(
                fill = SolidColor(Color(0xFFFFFFFF)),
                stroke = SolidColor(Color(0xFFFFFFFF)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(5F, 5F)
                lineTo(19F, 19F)
            }
        }.build()
        return _close24!!
    }

private var _close24: ImageVector? = null

@Preview
@Preview(fontScale = 3F)
@Composable
private fun Close24Preview() {
    ClientTheme {
        Icon(
            imageVector = Close24,
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}
