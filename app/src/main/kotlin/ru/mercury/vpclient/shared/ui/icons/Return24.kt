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
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

val Return24: ImageVector
    get() {
        if (_return24 != null) {
            return _return24!!
        }
        _return24 = ImageVector.Builder(
            name = "Return24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M4.47392 15.8416C3.88729 14.387 3.74251 12.7957 4.0516 11.2655C4.10628 10.9948
                    3.94962 10.7208 3.68289 10.6493C3.41615 10.5779 3.14053 10.736 3.08398
                    11.0063C2.71987 12.7465 2.87872 14.5598 3.54651 16.2156C4.27677 18.0263
                    5.57604 19.5504 7.24833 20.5581C8.92063 21.5657 10.8752 22.0022 12.8173
                    21.8017C14.7594 21.6013 16.5837 20.7747 18.0149 19.4467C19.4461 18.1187
                    20.4066 16.3614 20.7516 14.4397C21.0966 12.518 20.8074 10.5362 19.9275
                    8.79331C19.0476 7.05041 17.6248 5.6409 15.8737 4.7774C14.1227 3.91389
                    12.1383 3.64323 10.2199 4.00624L10.4058 4.98881C12.111 4.66613 13.8749
                    4.90672 15.4315 5.67428C16.988 6.44183 18.2527 7.69473 19.0348 9.24398C19.8169
                    10.7932 20.074 12.5548 19.7674 14.263C19.4607 15.9711 18.6069 17.5332
                    17.3347 18.7137C16.0625 19.8941 14.441 20.6288 12.7147 20.807C10.9884
                    20.9852 9.25092 20.5972 7.76444 19.7015C6.27795 18.8058 5.12305 17.4511
                    4.47392 15.8416Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M9.27885 4.90796C9.48027 5.09686 9.79669 5.08672 9.98559 4.8853L12.0378 2.69707C12.2267
                    2.49564 12.2166 2.17922 12.0152 1.99032C11.8137 1.80142 11.4973 1.81157
                    11.3084 2.01299L9.25618 4.20122C9.06728 4.40264 9.07743 4.71906 9.27885 4.90796Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M9.25566 4.19983C9.06592 4.40046 9.07476 4.71692 9.27539 4.90666L11.4551 6.96794C11.6557
                    7.15767 11.9722 7.14884 12.1619 6.9482C12.3516 6.74757 12.3428 6.43111
                    12.1422 6.24137L9.96249 4.18009C9.76185 3.99036 9.4454 3.99919 9.25566 4.19983Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.EvenOdd
            )
        }.build()
        return _return24!!
    }

private var _return24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Return24Preview() {
    Icon(
        imageVector = Return24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
