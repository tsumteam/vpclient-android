package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

val Phone22: ImageVector
    get() {
        if (_phone22 != null) {
            return _phone22!!
        }
        _phone22 = ImageVector.Builder(
            name = "Phone22",
            defaultWidth = 22.dp,
            defaultHeight = 22.dp,
            viewportWidth = 22F,
            viewportHeight = 22F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M18.3895,16.1533L15.2604,13.639C14.8631,13.3221 14.289,13.3637 13.9415,13.7344L12.8617,14.8941C12.7225,15.0472 12.4919,15.0747 12.3207,14.9586L12.1454,14.8406C11.564,14.4532 10.8406,13.9709 9.44514,12.3023C8.04968,10.6337 7.7019,9.83584 7.42311,9.19631L7.33844,9.003C7.25302,8.81385 7.31997,8.59068 7.4954,8.47977L8.82662,7.62185C9.25275,7.34517 9.3951,6.78765 9.15373,6.34056L7.23128,2.81667C6.98367,2.36122 6.42161,2.18111 5.95539,2.40777L4.49001,3.11961C4.03008,3.33767 3.6724,3.72529 3.49195,4.20123C2.82867,5.98178 2.92525,9.16518 7.15242,14.2186C10.5151,18.2377 13.1746,19.4748 15.0633,19.6428C15.4978,19.6833 15.9358,19.6653 16.3655,19.5892C16.8659,19.4957 17.3106,19.2121 17.6063,18.7977L18.5664,17.482C18.8722,17.063 18.7943,16.4776 18.3895,16.1533ZM18.1195,17.1589L17.1612,18.4757C16.9488,18.7749 16.6288,18.9803 16.2683,19.0489C14.5423,19.3615 11.6327,18.7165 7.57442,13.8655C3.51611,9.01459 3.39487,6.03714 4.00738,4.39314C4.1387,4.05002 4.39768,3.77096 4.73005,3.61439L6.19532,2.90365C6.39763,2.80519 6.64158,2.88334 6.74902,3.08098L7.79313,4.99742L8.6696,6.60441C8.77443,6.79836 8.71279,7.04031 8.52794,7.16047L7.19643,8.01837C6.79167,8.27444 6.6368,8.78914 6.8331,9.22605L6.916,9.41451C7.20892,10.0876 7.57304,10.925 9.02048,12.6548C10.4679,14.3847 11.2276,14.8909 11.8381,15.298L12.0092,15.4131C12.4046,15.6834 12.9386,15.6219 13.2621,15.2687L14.3415,14.1096C14.4925,13.9489 14.7414,13.931 14.9139,14.0683L18.0427,16.5826C18.2184,16.7232 18.2523,16.9772 18.1195,17.1589Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1B1B1B)),
                stroke = SolidColor(Color(0xFF1B1B1B)),
                strokeLineWidth = 0.5F,
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _phone22!!
    }

private var _phone22: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Phone22Preview() {
    Icon(
        imageVector = Phone22,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
