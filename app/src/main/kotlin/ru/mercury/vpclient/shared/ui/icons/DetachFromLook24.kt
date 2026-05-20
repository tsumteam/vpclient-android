package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

val DetachFromLook24: ImageVector
    get() {
        if (_detachFromLook24 != null) {
            return _detachFromLook24!!
        }
        _detachFromLook24 = ImageVector.Builder(
            name = "DetachFromLook24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M3.93634,7.5C3.80925,7.49972 3.68683,7.54785 3.59396,7.63459
                    C3.50108,7.72134 3.44472,7.84019 3.43634,7.967L2.57034,20.967
                    L2.56934,21C2.56934,21.1326 2.62201,21.2598 2.71578,21.3536
                    C2.80955,21.4473 2.93673,21.5 3.06934,21.5H20.9303L20.9633,21.499
                    C21.0289,21.4947 21.0929,21.4775 21.1518,21.4484
                    C21.2107,21.4193 21.2633,21.3789 21.3065,21.3295
                    C21.3498,21.2801 21.3829,21.2227 21.404,21.1605
                    C21.4251,21.0983 21.4337,21.0325 21.4293,20.967L20.5623,7.967
                    C20.554,7.84037 20.4977,7.72166 20.4051,7.63494
                    C20.3124,7.54822 20.1902,7.49998 20.0633,7.5L3.93634,7.5Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                stroke = SolidColor(Color(0xFFFFFFFF)),
                strokeLineWidth = 1F,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M11.9929,1.98999C15.9119,1.98999 17.3129,7.25299 17.4759,10.38
                    C17.4996,10.4472 17.5088,10.5186 17.5026,10.5896
                    C17.4965,10.6606 17.4753,10.7294 17.4404,10.7916
                    C17.4055,10.8537 17.3578,10.9076 17.3003,10.9498
                    C17.2429,10.9919 17.1771,11.0213 17.1074,11.036
                    C17.0377,11.0506 16.9656,11.0502 16.8961,11.0348
                    C16.8265,11.0193 16.7611,10.9892 16.7041,10.9464
                    C16.6472,10.9036 16.6,10.8491 16.5658,10.7866
                    C16.5316,10.7241 16.5112,10.655 16.5059,10.584L16.5069,10.588
                    C16.3889,8.31199 15.3229,3.02199 11.9929,3.02199
                    C8.65988,3.02199 7.61788,8.30699 7.50388,10.59
                    C7.48996,10.7215 7.42517,10.8424 7.32335,10.9269
                    C7.22153,11.0113 7.09074,11.0526 6.95888,11.042
                    C6.8934,11.0363 6.82967,11.0178 6.77135,10.9875
                    C6.71303,10.9572 6.66126,10.9157 6.619,10.8653
                    C6.57674,10.815 6.54482,10.7568 6.52507,10.6941
                    C6.50532,10.6314 6.49812,10.5655 6.50388,10.5L6.50488,10.466
                    C6.64688,7.34999 8.03788,1.98999 11.9929,1.98999Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _detachFromLook24!!
    }

private var _detachFromLook24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun DetachFromLook24Preview() {
    Icon(
        imageVector = DetachFromLook24,
        contentDescription = null,
        tint = Color.Black
    )
}
