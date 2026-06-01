package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

val CirclePlus24: ImageVector
    get() {
        if (_circlePlus24 != null) {
            return _circlePlus24!!
        }
        _circlePlus24 = ImageVector.Builder(
            name = "CirclePlus24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes("M12,1.5A10.5,10.5 0,1 1,12 22.5A10.5,10.5 0,1 1,12 1.5Z"),
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFFD76B6B)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero
            )
            path(
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFFD76B6B)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(12F, 6F)
                lineTo(12F, 18F)
            }
            path(
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFFD76B6B)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero,
                strokeLineCap = StrokeCap.Round
            ) {
                moveTo(6F, 12F)
                lineTo(18F, 12F)
            }
        }.build()
        return _circlePlus24!!
    }

private var _circlePlus24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CirclePlus24Preview() {
    Icon(
        imageVector = CirclePlus24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
