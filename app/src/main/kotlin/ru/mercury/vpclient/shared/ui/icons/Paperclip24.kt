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

val Paperclip24: ImageVector
    get() {
        if (_paperclip24 != null) {
            return _paperclip24!!
        }
        _paperclip24 = ImageVector.Builder(
            name = "Paperclip24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M21.2002 1.74835C18.8005 -0.582785 14.8962 -0.582785 12.4965 1.74835L3.33245 10.6504C3.04963 10.9251 3.04963 11.3702 3.33245 11.6449C3.61527 11.9197 4.07344 11.9197 4.35626 11.6449L13.5203 2.7429C15.2992 1.01621 18.3975 1.01621 20.1764 2.7429C22.0112 4.52523 22.0112 7.42576 20.1764 9.20809L7.38008 21.7785C6.28557 22.8418 4.37865 22.8418 3.28419 21.7785C2.15504 20.6817 2.15504 18.8973 3.28419 17.8004L15.5686 5.72649C15.9787 5.32815 16.6942 5.32815 17.1043 5.72649C17.5278 6.13787 17.5278 6.80757 17.1043 7.21899L5.3325 18.7949C5.08833 19.0321 5.08833 19.5522 5.3325 19.7895C5.61532 20.0642 6.07349 20.0642 6.35631 19.7895L18.1281 8.21349C19.1166 7.25326 19.1166 5.69213 18.1281 4.73195C17.1708 3.80199 15.5007 3.80335 14.5448 4.73195L2.26038 16.8058C0.579873 18.4383 0.579873 21.1405 2.26038 22.773C3.08128 23.5705 4.17222 24 5.33245 24C6.49268 24 7.58371 23.5705 8.40389 22.7731L21.2002 10.2026C23.5999 7.87145 23.5999 4.07945 21.2002 1.74835Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF6F757E)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _paperclip24!!
    }

private var _paperclip24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Paperclip24Preview() {
    Icon(
        imageVector = Paperclip24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
