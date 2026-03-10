package ru.mercury.vpclient.core.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.theme.ClientTheme

val ChevronStart24: ImageVector
    get() {
        if (_chevronStart24 != null) {
            return _chevronStart24!!
        }
        _chevronStart24 = ImageVector.Builder(
            name = "ChevronStart24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M6,11.9407C6,11.6815 6.08648,11.5085 6.25919,11.3356L15.3355,2.25926
                    C15.6812,1.91358 16.2863,1.91358 16.7185,2.25926C17.0641,2.60494 17.0641,3.21008
                    16.7185,3.64223L8.24798,11.9402L16.6322,20.3244C16.9779,20.6701 16.9779,21.2753
                    16.6322,21.7074C16.2865,22.1396 15.6814,22.0531 15.2492,21.7074L6.25987,12.6318
                    C6.0869,12.4588 6.00041,12.1994 6.00041,11.9401L6,11.9407Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF000000)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _chevronStart24!!
    }

private var _chevronStart24: ImageVector? = null

@Preview(showBackground = true)
@Composable
private fun ChevronStart24Preview() {
    ClientTheme {
        Icon(
            imageVector = ChevronStart24,
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}
