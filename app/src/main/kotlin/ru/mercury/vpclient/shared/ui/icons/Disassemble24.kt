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

val Disassemble24: ImageVector
    get() {
        if (_disassemble24 != null) {
            return _disassemble24!!
        }
        _disassemble24 = ImageVector.Builder(
            name = "Disassemble24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M12.0921,21.216C10.4714,21.216 8.8482,20.756 7.39784,19.8855
                    C6.47793,19.332 5.65132,18.6364 4.9491,17.8242L7.37206,17.9041
                    C7.65662,17.9136 7.89493,17.6904 7.90428,17.4057
                    C7.91363,17.1211 7.69062,16.8827 7.4059,16.8735L4.05353,16.7628
                    C3.86097,16.6835 3.63958,16.7278 3.4923,16.8752
                    C3.34519,17.0227 3.3012,17.2441 3.3808,17.4366L3.49117,20.7882
                    C3.49762,21.0729 3.73368,21.2985 4.0184,21.2922
                    C4.30296,21.2858 4.52887,21.0497 4.52242,20.7652
                    C4.52242,20.7613 4.52242,20.7577 4.5221,20.754L4.45829,18.8204
                    C5.1676,19.5784 5.97826,20.2347 6.86723,20.7702
                    C8.47775,21.7365 10.2845,22.2473 12.0921,22.2473
                    C14.9796,22.2473 17.6419,21.0566 19.5874,18.8949
                    C21.3109,16.9798 22.3398,14.4022 22.3398,11.9995
                    C22.3398,11.7148 22.1091,11.4839 21.8242,11.4839
                    C21.5393,11.4839 21.3086,11.7148 21.3086,11.9995
                    C21.3086,14.1544 20.3787,16.4742 18.8209,18.2049
                    C17.0732,20.1464 14.6838,21.216 12.0921,21.216Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M2.23031,12.5152C2.51519,12.5152 2.74593,12.2844 2.74593,11.9995
                    C2.74593,9.84488 3.67583,7.52489 5.23366,5.794
                    C6.98131,3.85251 9.37074,2.78323 11.9624,2.78323
                    C13.5831,2.78323 15.2063,3.24294 16.6567,4.11338
                    C17.5766,4.66703 18.4032,5.36264 19.1054,6.17475L16.6825,6.09483
                    C16.3979,6.08565 16.1596,6.30881 16.1502,6.59338
                    C16.1409,6.87794 16.3639,7.11625 16.6486,7.12576L20.001,7.23629
                    C20.1936,7.31557 20.4149,7.2711 20.5622,7.12382
                    C20.7093,6.97639 20.7533,6.75515 20.6737,6.5626L20.5634,3.21104
                    C20.5569,2.92615 20.3208,2.70041 20.0361,2.70685
                    C19.7516,2.71313 19.5257,2.9492 19.5321,3.23392
                    C19.5321,3.23762 19.5321,3.24117 19.5324,3.24504L19.5962,5.17863
                    C18.8869,4.4205 18.0763,3.76453 17.1873,3.22876
                    C15.5768,2.26229 13.77,1.75166 11.9624,1.75166
                    C9.07491,1.75166 6.41267,2.94211 4.46715,5.10386
                    C2.74367,7.01909 1.71468,9.59673 1.71468,11.9995
                    C1.71468,12.2844 1.94542,12.5152 2.23031,12.5152Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _disassemble24!!
    }

private var _disassemble24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Disassemble24Preview() {
    Icon(
        imageVector = Disassemble24,
        contentDescription = null,
        tint = Color.Black
    )
}
