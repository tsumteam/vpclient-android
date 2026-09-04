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

val ReplyOutline24: ImageVector
    get() {
        if (_replyOutline24 != null) {
            return _replyOutline24!!
        }
        _replyOutline24 = ImageVector.Builder(
            name = "ReplyOutline24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M2.7002,11.4941L9.81152,18.7617V14.4209H10.8115C14.8638,14.4209 18.7379,15.9965 21.5,18.7764
                    V17.3145C21.5,12.5165 17.316,8.56757 12.0791,8.56738H9.81152V4.22559L2.7002,11.4941Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color.Transparent),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            )
        }.build()
        return _replyOutline24!!
    }

private var _replyOutline24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun ReplyOutline24Preview() {
    Icon(
        imageVector = ReplyOutline24,
        contentDescription = null,
        tint = Color.Black
    )
}
