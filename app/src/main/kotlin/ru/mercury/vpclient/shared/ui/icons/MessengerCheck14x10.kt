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

val MessengerCheck14x10: ImageVector
    get() {
        if (_messengerCheck14x10 != null) {
            return _messengerCheck14x10!!
        }
        _messengerCheck14x10 = ImageVector.Builder(
            name = "MessengerCheck14x10",
            defaultWidth = 14.dp,
            defaultHeight = 10.dp,
            viewportWidth = 14F,
            viewportHeight = 10F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    "M0 5.42563L4.56609 10.0004L13.3338 1.24135L12.0751 0L4.56609 7.50028L1.24132 4.17554L0 5.42563Z"
                ),
                fill = SolidColor(Color(0xFF2F80ED)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _messengerCheck14x10!!
    }

private var _messengerCheck14x10: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun MessengerCheck14x10Preview() {
    Icon(
        imageVector = MessengerCheck14x10,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
