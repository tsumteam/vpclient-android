package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientTheme

val Sort24: ImageVector
    get() {
        if (_sort24 != null) {
            return _sort24!!
        }
        _sort24 = ImageVector.Builder(
            name = "Sort24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M8.59999 5.36574L6.0828 7.88293C5.92664 8.03909 5.67326 8.03909 5.51709 7.88293C5.36093 7.72663 5.36093 7.47338 5.51709 7.31721L8.71704 4.11712C8.87334 3.96096 9.12659 3.96096 9.28276 4.11712L12.4829 7.31721C12.639 7.47337 12.639 7.72662 12.4829 7.88293C12.3266 8.03909 12.0733 8.03909 11.9171 7.88293L9.39995 5.36574L9.39995 18C9.39995 18.2209 9.22088 18.4001 8.9999 18.4001C8.77905 18.4001 8.59999 18.2209 8.59999 18L8.59999 5.36574ZM15.8 17.8344L18.317 15.3172C18.4733 15.1611 18.7266 15.1611 18.8828 15.3172C19.0391 15.4734 19.0391 15.7268 18.8828 15.8829L15.6828 19.0829C15.5266 19.2392 15.2733 19.2392 15.1171 19.0829L11.9171 15.8829C11.7608 15.7268 11.7608 15.4734 11.9171 15.3172C12.0733 15.1611 12.3265 15.1611 12.4829 15.3172L14.9999 17.8344L14.9999 5.20014C14.9999 4.97916 15.1791 4.80009 15.4 4.80009C15.6208 4.80009 15.8 4.97916 15.8 5.20014L15.8 17.8344Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFFFFFFFF)),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _sort24!!
    }

private var _sort24: ImageVector? = null

@FontScalePreviews
@Composable
private fun Sort24Preview() {
    ClientTheme {
        Icon(
            imageVector = Sort24,
            contentDescription = null,
            tint = Color.Black
        )
    }
}
