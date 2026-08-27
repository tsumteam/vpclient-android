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
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

val PhoneCalling22: ImageVector
    get() {
        if (_phoneCalling22 != null) {
            return _phoneCalling22!!
        }
        _phoneCalling22 = ImageVector.Builder(
            name = "PhoneCalling22",
            defaultWidth = 22.dp,
            defaultHeight = 22.dp,
            viewportWidth = 22F,
            viewportHeight = 22F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M18.397 16.1426L15.2642 13.6328C14.8664 13.3166 14.2925 13.3589 13.9454 13.7302L12.8673 14.8915C12.7284 15.0448 12.4979 15.0726 12.3264 14.9568L12.151 14.839C11.569 14.4524 10.8449 13.9712 9.44702 12.3047C8.0491 10.6381 7.70015 9.84079 7.42042 9.20167L7.33547 9.00848C7.24977 8.81946 7.31639 8.59619 7.49166 8.48502L8.82161 7.62515C9.24734 7.34784 9.38887 6.79011 9.14684 6.34337L7.21922 2.82231C6.97093 2.36723 6.40862 2.18795 5.94272 2.41529L4.4784 3.12928C4.01879 3.34802 3.66167 3.73616 3.48193 4.21237C2.82126 5.99389 2.92252 9.17714 7.15711 14.2243C10.5257 18.2385 13.187 19.4716 15.0759 19.6369C15.5105 19.6768 15.9485 19.6581 16.3781 19.5814C16.8783 19.4872 17.3226 19.2029 17.6177 18.7881L18.5759 17.4709C18.8811 17.0515 18.8023 16.4663 18.397 16.1426ZM18.1285 17.1486L17.1722 18.4668C16.9602 18.7662 16.6405 18.9721 16.2801 19.0412C14.5545 19.3564 11.644 18.7157 7.57859 13.8707C3.51316 9.02569 3.38754 6.04842 3.99763 4.40352C4.12846 4.06021 4.38702 3.78076 4.71916 3.62371L6.18339 2.91081C6.38555 2.81206 6.62961 2.88985 6.73735 3.08733L7.78427 5.00224L8.6631 6.60794C8.76822 6.80173 8.70694 7.04377 8.52225 7.16421L7.19201 8.02406C6.78763 8.28072 6.63351 8.79566 6.83046 9.23227L6.91364 9.42061C7.20754 10.0933 7.57289 10.9301 9.02287 12.6578C10.4729 14.3855 11.2332 14.8906 11.8444 15.2968L12.0156 15.4117C12.4114 15.6815 12.9453 15.6191 13.2683 15.2655L14.3461 14.1048C14.4968 13.9439 14.7457 13.9256 14.9183 14.0626L18.0509 16.5724C18.2268 16.7127 18.261 16.9667 18.1285 17.1486Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1F1F1F)),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 0.331579F,
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M13.2915 5.95833C13.9672 6.09017 14.638 6.38818 15.1248 6.875C15.6117 7.36182 15.9097 8.03261 16.0415 8.70833M13.7498 2.75C15.1537 2.90596 16.4171 3.58515 17.4165 4.58333C18.4159 5.58152 19.0921 6.8463 19.2498 8.25
                    """.trimIndent()
                ),
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 1F,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            )
        }.build()
        return _phoneCalling22!!
    }

private var _phoneCalling22: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun PhoneCalling22Preview() {
    Icon(
        imageVector = PhoneCalling22,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
