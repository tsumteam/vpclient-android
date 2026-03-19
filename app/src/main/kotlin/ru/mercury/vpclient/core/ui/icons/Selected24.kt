package ru.mercury.vpclient.core.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientTheme

val Selected24: ImageVector
    get() {
        if (_selected24 != null) {
            return _selected24!!
        }
        _selected24 = ImageVector.Builder(
            name = "Selected24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M12,2C17.5228,2 22,6.47715 22,12C22,17.5228 17.5228,22 12,22C6.47715,22 2,17.5228 2,12
                    C2,6.47715 6.47715,2 12,2ZM10.8203,15.0469L6.88672,11.1133L5.70898,12.291L9.64258,16.2246V16.2256
                    L10.8213,17.4043L19.0713,9.15527L17.8926,7.97656L10.8203,15.0469Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _selected24!!
    }

private var _selected24: ImageVector? = null

@FontScalePreviews
@Composable
private fun Selected24Preview() {
    ClientTheme {
        Icon(
            imageVector = Selected24,
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}
