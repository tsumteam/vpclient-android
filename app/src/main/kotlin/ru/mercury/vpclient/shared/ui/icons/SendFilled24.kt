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

val SendFilled24: ImageVector
    get() {
        if (_sendFilled24 != null) {
            return _sendFilled24!!
        }
        _sendFilled24 = ImageVector.Builder(
            name = "SendFilled24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M9.29169,16.6767V20.5417C9.29169,20.8117 9.46502,21.0508 9.72169,21.1358
                    C9.78585,21.1567 9.85169,21.1667 9.91669,21.1667C10.1117,21.1667 10.3,21.075 10.42,20.9117
                    L12.6809,17.835L9.29169,16.6767Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M21.7371,2.11511C21.5454,1.97928 21.2938,1.96095 21.0854,2.07011L2.33544,11.8618
                    C2.11378,11.9776 1.98294,12.2143 2.00128,12.4634C2.02044,12.7134 2.18628,12.9268 2.42211,13.0076
                    L7.63461,14.7893L18.7354,5.29761L10.1454,15.6468L18.8813,18.6326
                    C18.9463,18.6543 19.0146,18.6659 19.0829,18.6659C19.1963,18.6659 19.3088,18.6351 19.4079,18.5751
                    C19.5663,18.4784 19.6738,18.3159 19.7013,18.1334L21.993,2.71678
                    C22.0271,2.48345 21.9288,2.25178 21.7371,2.11511Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _sendFilled24!!
    }

private var _sendFilled24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun SendFilled24Preview() {
    Icon(
        imageVector = SendFilled24,
        contentDescription = null,
        tint = Color.Black
    )
}
