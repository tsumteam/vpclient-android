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

val PrivacyPolicy24: ImageVector
    get() {
        if (_privacyPolicy24 != null) {
            return _privacyPolicy24!!
        }
        _privacyPolicy24 = ImageVector.Builder(
            name = "PrivacyPolicy24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes("M6.75 7.32193C6.75 7.096 6.87242 6.91284 7.02344 6.91284H8.44531C8.59633 6.91284 8.71875 7.096 8.71875 7.32193C8.71875 7.54787 8.59633 7.73102 8.44531 7.73102H7.02344C6.87242 7.73102 6.75 7.54787 6.75 7.32193Z"),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes("M10.0312 7.32193C10.0312 7.096 10.1694 6.91284 10.3397 6.91284H16.9415C17.1119 6.91284 17.25 7.096 17.25 7.32193C17.25 7.54787 17.1119 7.73102 16.9415 7.73102H10.3397C10.1694 7.73102 10.0312 7.54787 10.0312 7.32193Z"),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes("M6.75 12.0177C6.75 11.7918 6.87242 11.6086 7.02344 11.6086H8.44531C8.59633 11.6086 8.71875 11.7918 8.71875 12.0177C8.71875 12.2436 8.59633 12.4268 8.44531 12.4268H7.02344C6.87242 12.4268 6.75 12.2436 6.75 12.0177Z"),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes("M10.0312 12.0177C10.0312 11.7918 10.1694 11.6086 10.3397 11.6086H16.9415C17.1119 11.6086 17.25 11.7918 17.25 12.0177C17.25 12.2436 17.1119 12.4268 16.9415 12.4268H10.3397C10.1694 12.4268 10.0312 12.2436 10.0312 12.0177Z"),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes("M6.75 16.7133C6.75 16.4874 6.87242 16.3042 7.02344 16.3042H8.44531C8.59633 16.3042 8.71875 16.4874 8.71875 16.7133C8.71875 16.9392 8.59633 17.1224 8.44531 17.1224H7.02344C6.87242 17.1224 6.75 16.9392 6.75 16.7133Z"),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes("M10.0312 16.7133C10.0312 16.4874 10.1694 16.3042 10.3397 16.3042H16.9415C17.1119 16.3042 17.25 16.4874 17.25 16.7133C17.25 16.9392 17.1119 17.1224 16.9415 17.1224H10.3397C10.1694 17.1224 10.0312 16.9392 10.0312 16.7133Z"),
                fill = SolidColor(Color(0xFF1F1F1F)),
                pathFillType = PathFillType.EvenOdd
            )
            addPath(
                pathData = addPathNodes(
                    """
                    M18 2.9873C19.6642 2.9873 21.0127 4.39557 21.0127 6.13086V17.8691C21.0127
                    19.6044 19.6642 21.0127 18 21.0127H6C4.33575 21.0127 2.9873 19.6044
                    2.9873 17.8691V6.13086C2.9873 4.39557 4.33575 2.9873 6 2.9873H18ZM6
                    3.79492C4.76476 3.79492 3.7627 4.84059 3.7627 6.13086V17.8691C3.7627
                    19.1594 4.76476 20.2051 6 20.2051H18C19.2353 20.2051 20.2373 19.1594
                    20.2373 17.8691V6.13086C20.2373 4.84059 19.2353 3.79492 18 3.79492H6Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1F1F1F)),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 0.025F,
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _privacyPolicy24!!
    }

private var _privacyPolicy24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun PrivacyPolicy24Preview() {
    Icon(
        imageVector = PrivacyPolicy24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
