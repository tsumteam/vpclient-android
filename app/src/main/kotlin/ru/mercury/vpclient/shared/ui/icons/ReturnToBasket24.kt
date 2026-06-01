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
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

val ReturnToBasket24: ImageVector
    get() {
        if (_returnToBasket24 != null) {
            return _returnToBasket24!!
        }
        _returnToBasket24 = ImageVector.Builder(
            name = "ReturnToBasket24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M14.7027,19.9999L5.32217,19.9999C4.47736,19.9999 3.7925,19.3151 3.7925,18.4703
                    C3.7925,17.6254 4.47736,16.9406 5.32217,16.9406L14.7027,16.9406
                    C16.9772,16.9406 18.828,15.0907 18.828,12.8153
                    C18.828,10.5408 16.9772,8.69034 14.7027,8.69034L8.0799,8.69033
                    L8.0799,11.32L2,7.15999L8.07991,3L8.0799,5.63078L14.7027,5.63078
                    C18.6633,5.63078 21.8869,8.85455 21.8869,12.8154
                    C21.8869,16.7775 18.6635,19.9999 14.7027,19.9999Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF))
            )
        }.build()
        return _returnToBasket24!!
    }

private var _returnToBasket24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun ReturnToBasket24Preview() {
    Icon(
        imageVector = ReturnToBasket24,
        contentDescription = null,
        tint = Color.Black
    )
}
