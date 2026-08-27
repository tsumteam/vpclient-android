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
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

val ChevronDown24: ImageVector
    get() {
        if (_chevronDown24 != null) {
            return _chevronDown24!!
        }
        _chevronDown24 = ImageVector.Builder(
            name = "ChevronDown24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFFD76B6B)),
                strokeLineWidth = 3F,
                pathFillType = PathFillType.NonZero,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(3.05333F, 9.0723F)
                lineTo(11.9486F, 14.0F)
            }
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFFD76B6B)),
                strokeLineWidth = 3F,
                pathFillType = PathFillType.NonZero,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(20.9468F, 9.0713F)
                lineTo(12.0516F, 13.9988F)
            }
        }.build()
        return _chevronDown24!!
    }

private var _chevronDown24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun ChevronDown24Preview() {
    Icon(
        imageVector = ChevronDown24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
