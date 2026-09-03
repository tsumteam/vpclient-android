package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

val Repeat24: ImageVector
    get() {
        if (_repeat24 != null) {
            return _repeat24!!
        }
        _repeat24 = ImageVector.Builder(
            name = "Repeat24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M20.1855 5.81934L14.0195 10.6162V7.66699H13.0693C9.37891 7.667 5.91045 9.8251
                    3.5 11.6846V10.8662C3.5 9.30133 4.40281 7.57961 5.94434 6.2334C7.47748 4.89459
                    9.595 3.97179 11.9287 3.97168H14.0195V1.02148L20.1855 5.81934Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 1F
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M3.81445 18.584L9.98047 13.7871V16.7363H10.9307C14.6211 16.7363 18.0896
                    14.5782 20.5 12.7188V13.5371C20.5 15.102 19.5972 16.8237 18.0557
                    18.1699C16.5225 19.5087 14.405 20.4315 12.0713 20.4316H9.98047V23.3818L3.81445
                    18.584Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 1F
            )
        }.build()
        return _repeat24!!
    }

private var _repeat24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Repeat24Preview() {
    Icon(
        imageVector = Repeat24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
