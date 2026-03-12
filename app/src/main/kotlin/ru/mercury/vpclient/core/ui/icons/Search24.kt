package ru.mercury.vpclient.core.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.theme.ClientTheme

val Search24: ImageVector
    get() {
        if (_search24 != null) {
            return _search24!!
        }
        _search24 = ImageVector.Builder(
            name = "Search24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M10.875,3.5C14.948,3.5 18.25,6.802 18.25,10.875C18.25,14.948 14.948,18.25 10.875,18.25C6.802,18.25 3.5,14.948 3.5,10.875C3.5,6.802 6.802,3.5 10.875,3.5Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M16.092,16.092L21.567,21.567
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 1F,
                strokeLineCap = StrokeCap.Round,
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _search24!!
    }

private var _search24: ImageVector? = null

@Preview(showBackground = true)
@Composable
private fun Search24Preview() {
    ClientTheme {
        Icon(
            imageVector = Search24,
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}
