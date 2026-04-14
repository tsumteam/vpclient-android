package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientTheme

val Phone24: ImageVector
    get() {
        if (_phone24 != null) {
            return _phone24!!
        }
        _phone24 = ImageVector.Builder(
            name = "Phone24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M20.061,17.622L16.647,14.879C16.214,14.533 15.588,14.579 15.209,14.983L14.031,16.248
                    C13.879,16.415 13.627,16.445 13.441,16.319L13.249,16.19C12.615,15.767 11.826,15.241 10.304,13.421
                    C8.781,11.6 8.402,10.73 8.098,10.032L8.005,9.822C7.912,9.615 7.985,9.372 8.177,9.251L9.629,8.315
                    C10.094,8.013 10.249,7.405 9.986,6.917L7.889,3.073C7.618,2.576 7.005,2.379 6.497,2.627L4.898,3.403
                    C4.396,3.641 4.006,4.064 3.809,4.583C3.086,6.526 3.191,9.998 7.803,15.511C11.471,19.896 14.372,21.245
                    16.433,21.429C16.906,21.473 17.384,21.453 17.853,21.37C18.399,21.268 18.884,20.959 19.207,20.507
                    L20.254,19.071C20.588,18.614 20.503,17.976 20.061,17.622ZM19.767,18.719L18.721,20.155C18.489,20.482
                    18.14,20.706 17.747,20.781C15.864,21.122 12.69,20.418 8.263,15.126C3.836,9.834 3.703,6.586 4.372,4.793
                    C4.515,4.418 4.797,4.114 5.16,3.943L6.758,3.168C6.979,3.06 7.245,3.146 7.362,3.361L8.501,5.452L9.458,7.205
                    C9.572,7.416 9.505,7.68 9.303,7.811L7.851,8.747C7.409,9.027 7.24,9.588 7.454,10.065L7.545,10.27
                    C7.864,11.005 8.261,11.918 9.84,13.805C11.419,15.692 12.248,16.245 12.914,16.689L13.101,16.814
                    C13.532,17.109 14.115,17.042 14.468,16.657L15.645,15.392C15.81,15.217 16.081,15.198 16.27,15.347
                    L19.683,18.09C19.875,18.244 19.912,18.521 19.767,18.719Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1F1F1F)),
                stroke = SolidColor(Color(0xFF1F1F1F)),
                strokeLineWidth = 0.5F,
                pathFillType = androidx.compose.ui.graphics.PathFillType.NonZero
            )
        }.build()
        return _phone24!!
    }

private var _phone24: ImageVector? = null

@FontScalePreviews
@Composable
private fun Phone24Preview() {
    ClientTheme {
        Icon(
            imageVector = Phone24,
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}
