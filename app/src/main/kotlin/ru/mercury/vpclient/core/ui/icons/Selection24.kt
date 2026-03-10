package ru.mercury.vpclient.core.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.theme.ClientTheme

val Selection24: ImageVector
    get() {
        if (_selection24 != null) {
            return _selection24!!
        }
        _selection24 = ImageVector.Builder(
            name = "Selection24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M7.741,2.571C7.487,2.57 7.106,2.613 6.867,2.796C6.481,3.105 6.392,3.259 5.596,3.741
                    C4.17,4.459 2.861,4.628 2.554,5.297C2.233,6.353 3.155,8.278 3.964,9.737C3.964,9.737 4.372,10.784
                    4.416,11.854C4.532,13.123 3.468,14.464 3.803,17.435C3.803,17.435 4.164,21.026 4.555,21.817
                    C4.961,22.36 7.208,22.449 7.719,22.453M7.572,2.571C7.826,2.57 8.207,2.613 8.446,2.796C8.832,3.105
                    8.921,3.259 9.717,3.741C11.143,4.459 12.452,4.628 12.759,5.297C13.08,6.353 12.158,8.278 11.349,9.737
                    C11.349,9.737 10.941,10.784 10.897,11.854C10.781,13.123 11.845,14.464 11.51,17.435C11.51,17.435
                    11.149,21.026 10.758,21.817C10.352,22.36 8.105,22.449 7.594,22.453
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 1F,
                pathFillType = androidx.compose.ui.graphics.PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M11.317,2.571C13.076,3.315 15.637,3.532 16.157,4.776C16.761,6.125 15.489,8.433 14.518,9.911
                    C14.518,9.911 13.977,11.158 13.923,12.243C13.785,13.527 15.455,15.15 15.067,17.269C15.067,17.269
                    14.377,20.86 13.923,21.865C13.819,22.14 13.762,22.233 13.658,22.453
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 1F,
                strokeLineCap = StrokeCap.Round,
                pathFillType = androidx.compose.ui.graphics.PathFillType.NonZero
            )
        }.build()
        return _selection24!!
    }

private var _selection24: ImageVector? = null

@Preview(showBackground = true)
@Composable
private fun Selection24Preview() {
    ClientTheme {
        Icon(
            imageVector = Selection24,
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}
