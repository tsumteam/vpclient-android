package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

val Qr24: ImageVector
    get() {
        if (_qr24 != null) {
            return _qr24!!
        }
        _qr24 = ImageVector.Builder(
            name = "Qr24",
            defaultWidth = 25.dp,
            defaultHeight = 25.dp,
            viewportWidth = 25F,
            viewportHeight = 25F
        ).apply {
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = .8F,
                strokeLineCap = StrokeCap.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(.400391F, 10.1157F)
                verticalLineTo(.971533F)
                curveTo(.400391F, .78103F, .514443F, .400024F, .970652F, .400024F)
                horizontalLineTo(10.0948F)
                moveTo(.400391F, 14.6844F)
                verticalLineTo(23.8285F)
                curveTo(.400391F, 24.019F, .514443F, 24.4F, .970652F, 24.4F)
                horizontalLineTo(10.0948F)
                moveTo(24.4004F, 10.1157F)
                verticalLineTo(.971533F)
                curveTo(24.4004F, .78103F, 24.2863F, .400024F, 23.8301F, .400024F)
                horizontalLineTo(14.7059F)
                moveTo(24.4004F, 14.6844F)
                verticalLineTo(23.8285F)
                curveTo(24.4004F, 24.019F, 24.2863F, 24.4F, 23.8301F, 24.4F)
                horizontalLineTo(14.7059F)
            }
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = .8F,
                strokeLineCap = StrokeCap.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(3.64355F, 3.67944F)
                horizontalLineTo(10.6114F)
                verticalLineTo(10.5699F)
                horizontalLineTo(3.64355F)
                close()
                moveTo(3.64355F, 14.0615F)
                horizontalLineTo(10.6114F)
                verticalLineTo(20.952F)
                horizontalLineTo(3.64355F)
                close()
                moveTo(14.1895F, 3.67944F)
                horizontalLineTo(21.1574F)
                verticalLineTo(10.5699F)
                horizontalLineTo(14.1895F)
                close()
                moveTo(21.1572F, 15.6588F)
                verticalLineTo(16.2225F)
                moveTo(16.7959F, 17.965F)
                verticalLineTo(18.5287F)
                moveTo(17.8955F, 19.4316F)
                verticalLineTo(20.3848F)
                moveTo(14.7773F, 19.5422F)
                horizontalLineTo(15.3398F)
                moveTo(16.6256F, 14.4615F)
                verticalLineTo(15.5022F)
                moveTo(15.379F, 15.8051F)
                horizontalLineTo(18.1714F)
                moveTo(12.4072F, 14.8909F)
                verticalLineTo(20.952F)
                moveTo(12.4023F, 20.952F)
                horizontalLineTo(13.7752F)
                moveTo(12.4023F, 16.8611F)
                horizontalLineTo(13.7752F)
                moveTo(21.1551F, 18.0297F)
                lineTo(21.1572F, 20.8335F)
                moveTo(16.6221F, 20.9497F)
                horizontalLineTo(21.1589F)
            }
        }.build()
        return _qr24!!
    }

private var _qr24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Qr24Preview() {
    Icon(
        imageVector = Qr24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
