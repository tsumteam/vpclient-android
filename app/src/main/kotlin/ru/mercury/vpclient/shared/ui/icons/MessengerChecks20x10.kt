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

val MessengerChecks20x10: ImageVector
    get() {
        if (_messengerChecks20x10 != null) {
            return _messengerChecks20x10!!
        }
        _messengerChecks20x10 = ImageVector.Builder(
            name = "MessengerChecks20x10",
            defaultWidth = 20.dp,
            defaultHeight = 10.dp,
            viewportWidth = 20F,
            viewportHeight = 10F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    "M8.74026 7.50034L11.2322 10.0004L20 1.24135L18.7412 0L11.2322 7.50028L10.0001 6.26815L8.74026 7.50034Z"
                ),
                fill = SolidColor(Color(0xFF2F80ED)),
                pathFillType = PathFillType.NonZero
            )
            addPath(
                pathData = addPathNodes(
                    "M0 5.42563L4.56609 10.0004L13.3338 1.24135L12.0751 0L4.56609 7.50028L1.24132 4.17554L0 5.42563Z"
                ),
                fill = SolidColor(Color(0xFF2F80ED)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _messengerChecks20x10!!
    }

private var _messengerChecks20x10: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun MessengerChecks20x10Preview() {
    Icon(
        imageVector = MessengerChecks20x10,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
