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

val Edit24: ImageVector
    get() {
        if (_edit24 != null) {
            return _edit24!!
        }
        _edit24 = ImageVector.Builder(
            name = "Edit24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M4.38216,19.959C3.97884,20.0486 3.61977,19.6874 3.71182,19.2846L4.351,16.4875
                    C4.44531,16.0748 4.65413,15.6971 4.95347,15.3978L12.9167,7.43456L16.2341,10.752
                    L8.24587,18.7403C7.94325,19.0429 7.56059,19.2529 7.1428,19.3458L4.38216,19.959Z
                    M13.5802,6.77106L16.41,3.94124C16.7761,3.57513 17.3697,3.57513 17.7358,3.94124
                    C17.738,3.9434 19.5301,5.77074 19.5301,5.77074C19.821,6.06744 19.896,6.2051 19.9449,6.37032
                    C19.9938,6.53554 19.993,6.70409 19.9425,6.86883C19.8919,7.03356 19.8157,7.17049 19.5218,7.46434
                    L16.8976,10.0885L13.5802,6.77106Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.EvenOdd
            )
        }.build()
        return _edit24!!
    }

private var _edit24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Edit24Preview() {
    Icon(
        imageVector = Edit24,
        contentDescription = null,
        tint = Color.Black
    )
}
