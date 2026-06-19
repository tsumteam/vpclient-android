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

val Notification24: ImageVector
    get() {
        if (_notification24 != null) {
            return _notification24!!
        }
        _notification24 = ImageVector.Builder(
            name = "Notification24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M11.6426 0.950195C12.5345 0.950195 13.2636 1.67938 13.2637 2.57129C13.2637
                    2.90744 13.1926 3.1761 13.0205 3.44043C16.4667 4.10393 19.1571 7.2485 19.1572
                    10.9785V16.8721C19.1573 16.9426 19.1748 17.0132 19.2051 17.0693C19.2356 17.1259
                    19.2764 17.1634 19.3193 17.1777L19.3477 17.1875V17.1895C20.9514 17.8204 20.5217
                    20.3004 18.6357 20.2998H14.7881C14.5692 21.8356 13.2248 23.0498 11.6426 23.0498C10.0605
                    23.0497 8.71619 21.8356 8.49707 20.2998H4.64941C2.72772 20.2995 2.34677 17.7854
                    3.96387 17.1777H3.96582C4.00878 17.1635 4.04952 17.1251 4.08008 17.0684C4.11032
                    17.0122 4.12793 16.9415 4.12793 16.8711V10.8213C4.12798 8.76478 4.9586 6.82691
                    6.42188 5.40332L6.42285 5.40234C7.51192 4.39108 8.83245 3.68575 10.2705 3.4375C10.1279
                    3.17301 10.0215 2.87391 10.0215 2.57129C10.0215 1.67944 10.7507 0.950287 11.6426
                    0.950195ZM9.3877 20.3008C9.60304 21.3499 10.511 22.165 11.6426 22.165C12.7742 22.165
                    13.6832 21.35 13.8984 20.3008H9.3877ZM11.6426 4.19238C9.92706 4.1924 8.28975
                    4.85565 7.04199 6.06445H7.04102C5.71545 7.31199 5.01369 8.98812 5.01367 10.8213V16.8721C5.01353
                    17.3601 4.72944 17.8101 4.27832 17.9766C3.87088 18.211 3.77406 18.5699 3.86816
                    18.8672C3.96354 19.1683 4.25711 19.414 4.64941 19.4141H18.6357L18.8008 19.4014C19.1645
                    19.3409 19.3796 19.0815 19.4385 18.7969C19.5061 18.4691 19.3669 18.1126 19.0107
                    17.9785C18.5574 17.8137 18.2716 17.3614 18.2715 16.8721V10.9785C18.2714 7.33873
                    15.3423 4.19238 11.6426 4.19238ZM11.6426 1.83594C11.2381 1.83599 10.9072 2.16703
                    10.9072 2.57129C10.9073 2.97573 11.2381 3.30659 11.6426 3.30664C12.0471 3.30664
                    12.3778 2.97576 12.3779 2.57129C12.3779 2.16675 12.0471 1.83594 11.6426 1.83594Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _notification24!!
    }

private var _notification24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Notification24Preview() {
    Icon(
        imageVector = Notification24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
