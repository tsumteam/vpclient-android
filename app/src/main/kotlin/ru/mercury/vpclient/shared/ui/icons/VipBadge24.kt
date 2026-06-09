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

val VipBadge24: ImageVector
    get() {
        if (_vipBadge24 != null) {
            return _vipBadge24!!
        }
        _vipBadge24 = ImageVector.Builder(
            name = "VipBadge24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M3 4H21C22.6569 4 24 5.34315 24 7V17C24 18.6569 22.6569 20 21 20H3C1.34315
                    20 0 18.6569 0 17V7C0 5.34315 1.34315 4 3 4Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1B1B1B)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M8.2738 14.9693H6.67463L4 9H5.86102L7.48824 12.8665L9.12482 9H10.9578L8.2738 14.9693Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M13.4045 14.9354H11.7211V9H13.4045V14.9354Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M16.3808 14.9354H14.6975V9H17.6153C18.351 9 18.9308 9.17241 19.3547 9.51723C19.7849
                    9.86205 20 10.3086 20 10.8569C20 11.4053 19.7849 11.8546 19.3547 12.2051C18.9308
                    12.5499 18.351 12.7223 17.6153 12.7223H16.3808V14.9354ZM16.3808 10.2888V11.4674H17.4937C17.7369
                    11.4674 17.9301 11.4166 18.0735 11.3148C18.2231 11.2074 18.298 11.0604 18.298 10.8739C18.298
                    10.6874 18.2263 10.5432 18.0829 10.4415C17.9395 10.3397 17.7431 10.2888 17.4937 10.2888H16.3808Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _vipBadge24!!
    }

private var _vipBadge24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun VipBadge24Preview() {
    Icon(
        imageVector = VipBadge24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
