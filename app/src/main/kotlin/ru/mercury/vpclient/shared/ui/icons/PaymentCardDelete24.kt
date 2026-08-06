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

val PaymentCardDelete24: ImageVector
    get() {
        if (_paymentCardDelete24 != null) {
            return _paymentCardDelete24!!
        }
        _paymentCardDelete24 = ImageVector.Builder(
            name = "PaymentCardDelete24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M15.3106,8.69482C15.0001,8.69482 14.7485,8.94643 14.7485,9.25689
                    L14.7485,19.8798C14.7485,20.1901 15.0001,20.4419 15.3106,20.4419
                    C15.621,20.4419 15.8727,20.1901 15.8727,19.8798L15.8727,9.25689
                    C15.8727,8.94643 15.621,8.69482 15.3106,8.69482Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF))
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M8.67827,8.69482C8.36782,8.69482 8.11621,8.94643 8.11621,9.25689
                    L8.11621,19.8798C8.11621,20.1901 8.36782,20.4419 8.67827,20.4419
                    C8.98872,20.4419 9.24033,20.1901 9.24033,19.8798L9.24033,9.25689
                    C9.24033,8.94643 8.98872,8.69482 8.67827,8.69482Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF))
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M3.84439,7.14503L3.84439,20.993C3.84439,21.8115 4.14452,22.5801 4.66881,23.1317
                    C5.19069,23.6847 5.91698,23.9987 6.67708,24L17.3114,24
                    C18.0717,23.9987 18.798,23.6847 19.3197,23.1317C19.844,22.5801 20.1441,21.8115 20.1441,20.993
                    L20.1441,7.14503C21.1863,6.86839 21.8617,5.86152 21.7223,4.79207
                    C21.5826,3.72284 20.6717,2.923 19.5932,2.92278L16.7155,2.92278L16.7155,2.22021
                    C16.7188,1.62939 16.4852,1.06206 16.067,0.644686C15.6487,0.227532 15.0805,-0.00475635 14.4897,0.00007385
                    L9.49879,0.00007385C8.90797,-0.00475635 8.33976,0.227532 7.92151,0.644686
                    C7.50326,1.06206 7.26965,1.62939 7.27295,2.22021L7.27295,2.92278L4.39525,2.92278
                    C3.3168,2.923 2.40587,3.72284 2.26623,4.79207C2.12681,5.86152 2.80216,6.86839 3.84439,7.14503Z
                    M17.3114,22.8759L6.67708,22.8759C5.71609,22.8759 4.9685,22.0504 4.9685,20.993
                    L4.9685,7.19443L19.02,7.19443L19.02,20.993C19.02,22.0504 18.2724,22.8759 17.3114,22.8759Z
                    M8.39706,2.22021C8.39333,1.92754 8.50838,1.64585 8.71608,1.43925
                    C8.92356,1.23265 9.2059,1.11914 9.49879,1.12419L14.4897,1.12419
                    C14.7826,1.11914 15.0649,1.23265 15.2724,1.43925C15.4801,1.64563 15.5952,1.92754 15.5914,2.22021
                    L15.5914,2.92278L8.39706,2.92278L8.39706,2.22021Z
                    M4.39525,4.0469L19.5932,4.0469C20.152,4.0469 20.6049,4.49984 20.6049,5.05861
                    C20.6049,5.61737 20.152,6.07031 19.5932,6.07031L4.39525,6.07031
                    C3.83648,6.07031 3.38354,5.61737 3.38354,5.05861C3.38354,4.49984 3.83648,4.0469 4.39525,4.0469Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF))
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M11.9942,8.69482C11.6837,8.69482 11.4321,8.94643 11.4321,9.25689
                    L11.4321,19.8798C11.4321,20.1901 11.6837,20.4419 11.9942,20.4419
                    C12.3046,20.4419 12.5562,20.1901 12.5562,19.8798L12.5562,9.25689
                    C12.5562,8.94643 12.3046,8.69482 11.9942,8.69482Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF))
            )
        }.build()
        return _paymentCardDelete24!!
    }

private var _paymentCardDelete24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun PaymentCardDelete24Preview() {
    Icon(
        imageVector = PaymentCardDelete24,
        contentDescription = null,
        tint = Color(0xFFD76B6B)
    )
}
