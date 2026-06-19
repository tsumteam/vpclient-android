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

val Contacts24: ImageVector
    get() {
        if (_contacts24 != null) {
            return _contacts24!!
        }
        _contacts24 = ImageVector.Builder(
            name = "Contacts24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M20.0612 17.6219L16.6476 14.8789C16.2142 14.5333 15.5879 14.5786 15.2088 14.9831L14.0308
                    16.2482C13.879 16.4152 13.6274 16.4452 13.4406 16.3185L13.2494 16.1898C12.6152 15.7672
                    11.826 15.241 10.3037 13.4207C8.78138 11.6004 8.40198 10.7301 8.09785 10.0324L8.00549
                    9.82149C7.91229 9.61516 7.98534 9.3717 8.17671 9.2507L9.62895 8.31479C10.0938 8.01296
                    10.2491 7.40475 9.9858 6.91702L7.88858 3.07277C7.61846 2.57593 7.00531 2.37944 6.4967
                    2.6267L4.89811 3.40325C4.39636 3.64114 4.00616 4.064 3.80932 4.58321C3.08573 6.52562
                    3.1911 9.99842 7.80256 15.5112C11.4709 19.8958 14.3722 21.2452 16.4326 21.4285C16.9066
                    21.4728 17.3844 21.4531 17.8532 21.37C18.3991 21.2681 18.8842 20.9587 19.2068 20.5067L20.2542
                    19.0713C20.5878 18.6142 20.5028 17.9756 20.0612 17.6219ZM19.7666 18.7189L18.7213 20.1554C18.4895
                    20.4817 18.1404 20.7058 17.7472 20.7806C15.8642 21.1217 12.6901 20.418 8.26291 15.1261C3.83567
                    9.83415 3.7034 6.58601 4.3716 4.79257C4.51486 4.41825 4.79738 4.11382 5.15997 3.94302L6.75845
                    3.16766C6.97914 3.06025 7.24527 3.14551 7.36248 3.36111L8.5015 5.45178L9.45766 7.20486C9.57202
                    7.41643 9.50478 7.68038 9.30311 7.81147L7.85056 8.74736C7.40901 9.0267 7.24005 9.5882 7.45421
                    10.0648L7.54464 10.2704C7.86418 11.0047 8.26141 11.9182 9.84043 13.8053C11.4195 15.6924
                    12.2482 16.2447 12.9142 16.6887L13.1009 16.8144C13.5322 17.1093 14.1147 17.0421 14.4676 16.6568L15.6452
                    15.3923C15.8099 15.2171 16.0815 15.1975 16.2696 15.3473L19.6829 18.0902C19.8746 18.2435 19.9115
                    18.5207 19.7666 18.7189Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1F1F1F)),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 0.5F,
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _contacts24!!
    }

private var _contacts24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Contacts24Preview() {
    Icon(
        imageVector = Contacts24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
