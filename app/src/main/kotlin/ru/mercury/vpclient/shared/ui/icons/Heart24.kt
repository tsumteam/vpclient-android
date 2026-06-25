package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

val Heart24: ImageVector
    get() {
        if (_heart24 != null) {
            return _heart24!!
        }
        _heart24 = ImageVector.Builder(
            name = "Heart24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFF1B1B1B)),
                strokeLineWidth = 1F,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(17.0977F, 2.5F)
                curveTo(20.435F, 2.50003F, 22.5F, 5.41099F, 22.5F, 7.79004F)
                curveTo(22.4998F, 10.4236F, 20.9888F, 13.0252F, 18.835F, 15.4258F)
                curveTo(16.783F, 17.7128F, 14.2076F, 19.7486F, 12F, 21.377F)
                curveTo(9.79248F, 19.7487F, 7.21701F, 17.7137F, 5.16504F, 15.4268F)
                curveTo(3.0111F, 13.0262F, 1.50016F, 10.424F, 1.5F, 7.79004F)
                curveTo(1.5F, 5.41077F, 3.56488F, 2.50003F, 6.90234F, 2.5F)
                curveTo(7.78781F, 2.5F, 8.63653F, 2.85086F, 9.44043F, 3.41211F)
                curveTo(10.245F, 3.97388F, 10.9763F, 4.72711F, 11.6191F, 5.48242F)
                lineTo(12F, 5.92969F)
                lineTo(12.3809F, 5.48242F)
                curveTo(13.0238F, 4.72703F, 13.7559F, 3.9739F, 14.5605F, 3.41211F)
                curveTo(15.3644F, 2.85097F, 16.2123F, 2.5F, 17.0977F, 2.5F)
                close()
            }
        }.build()
        return _heart24!!
    }

private var _heart24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun Heart24Preview() {
    Icon(
        imageVector = Heart24,
        contentDescription = null
    )
}
