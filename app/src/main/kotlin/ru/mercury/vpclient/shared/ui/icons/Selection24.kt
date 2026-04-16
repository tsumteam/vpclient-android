package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientTheme

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
                    M10.2528,2.57143C9.99908,2.57047,9.61721,2.61316,9.37906,2.79643
                    C8.99272,3.10554,8.9041,3.25904,8.10734,3.74119C6.68193,4.45957,5.37262,4.62768,5.0652,5.29733
                    C4.7446,6.35301,5.66631,8.27807,6.47514,9.73718C6.47514,9.73718,6.88323,10.7838,6.92783,11.8545
                    C7.04353,13.1229,5.9797,14.464,6.31477,17.4351C6.31477,17.4351,6.67526,21.0257,7.06672,21.8167
                    C7.4725,22.3596,9.7198,22.4491,10.2302,22.4531
                    M10.0835,2.57143C10.3372,2.57047,10.7191,2.61316,10.9572,2.79643
                    C11.3436,3.10554,11.4322,3.25904,12.2289,3.74119C13.6543,4.45957,14.9637,4.62768,15.2711,5.29733
                    C15.5917,6.35301,14.67,8.27807,13.8611,9.73718C13.8611,9.73718,13.453,10.7838,13.4084,11.8545
                    C13.2928,13.1229,14.3566,14.464,14.0215,17.4351C14.0215,17.4351,13.661,21.0257,13.2696,21.8167
                    C12.8638,22.3596,10.6165,22.4491,10.106,22.4531
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
                    M13.8293,2.57141C15.5883,3.3151,18.1494,3.53246,18.6694,4.77574
                    C19.2731,6.12512,18.0008,8.43326,17.0301,9.91098C17.0301,9.91098,16.4889,11.1585,16.4354,12.2429
                    C16.2965,13.5274,17.9668,15.1503,17.5787,17.2688C17.5787,17.2688,16.8893,20.8599,16.4354,21.8651
                    C16.3306,22.1404,16.2744,22.2328,16.1696,22.4531
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

@FontScalePreviews
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
