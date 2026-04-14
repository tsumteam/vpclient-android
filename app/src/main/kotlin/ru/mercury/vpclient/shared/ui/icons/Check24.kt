package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientTheme

val Check24: ImageVector
    get() {
        if (_check24 != null) {
            return _check24!!
        }
        _check24 = ImageVector.Builder(
            name = "Check24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M19.0713,9.15503L11.999,16.2253L12,16.2263L10.8213,17.4041L9.64258,16.2263V16.2253L4.92871,11.5115
                    L6.10742,10.3337L10.8213,15.0476L17.8926,7.97632L19.0713,9.15503Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _check24!!
    }

private var _check24: ImageVector? = null

@FontScalePreviews
@Composable
private fun Check24Preview() {
    ClientTheme {
        Icon(
            imageVector = Check24,
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}
