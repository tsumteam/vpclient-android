package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

val EditOutline24: ImageVector
    get() {
        if (_editOutline24 != null) {
            return _editOutline24!!
        }
        _editOutline24 = ImageVector.Builder(
            name = "EditOutline24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M3.86017,15.4966L2.55985,20.6738C2.44354,21.1369 2.86332,21.5566 3.32638,21.4402
                    L8.50259,20.139L21.2474,7.39423C21.7407,6.90093 21.7407,6.10115 21.2474,5.60785
                    L18.3913,2.75181C17.898,2.25852 17.0982,2.25852 16.605,2.75181L3.86017,15.4966Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            )
            addPath(
                pathData = addPathNodes("M14.8164,4.53906L19.4588,9.18148"),
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            )
        }.build()
        return _editOutline24!!
    }

private var _editOutline24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun EditOutline24Preview() {
    Icon(
        imageVector = EditOutline24,
        contentDescription = null,
        tint = Color.Black
    )
}
