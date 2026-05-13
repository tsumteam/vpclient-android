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

val Card24: ImageVector
    get() {
        if (_card24 != null) {
            return _card24!!
        }
        _card24 = ImageVector.Builder(
            name = "Card24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFF97A0AE)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(5F, 1.5F)
                horizontalLineTo(19F)
                arcTo(3.5F, 3.5F, 0F,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    x1 = 22.5F,
                    y1 = 5F
                )
                verticalLineTo(15F)
                arcTo(3.5F, 3.5F, 0F,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    x1 = 19F,
                    y1 = 18.5F
                )
                horizontalLineTo(5F)
                arcTo(3.5F, 3.5F, 0F,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    x1 = 1.5F,
                    y1 = 15F
                )
                verticalLineTo(5F)
                arcTo(3.5F, 3.5F, 0F,
                    isMoreThanHalf = false,
                    isPositiveArc = true,
                    x1 = 5F,
                    y1 = 1.5F
                )
                close()
            }
            path(
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFF97A0AE)),
                strokeLineWidth = 1F,
                strokeLineCap = StrokeCap.Round,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(4.5F, 22.5F)
                horizontalLineTo(19.5F)
            }
        }.build()
        return _card24!!
    }

private var _card24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Card24Preview() {
    Icon(
        imageVector = Card24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
