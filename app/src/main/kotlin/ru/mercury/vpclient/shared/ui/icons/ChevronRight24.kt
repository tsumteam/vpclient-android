package ru.mercury.vpclient.shared.ui.icons

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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

val ChevronRight24: ImageVector
    get() {
        if (_chevronRight24 != null) {
            return _chevronRight24!!
        }
        _chevronRight24 = ImageVector.Builder(
            name = "ChevronRight24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M17.0107,11.9526C17.0107,11.7452 16.9398,11.6068 16.7982,11.4684L9.35711,4.20741
                    C9.07371,3.93086 8.57759,3.93086 8.22329,4.20741C7.93989,4.48395 7.93989,4.96807
                    8.22329,5.31379L15.1678,11.9522L8.29401,18.6596C8.01061,18.9361 8.01061,19.4202
                    8.29401,19.7659C8.57741,20.1117 9.07353,20.0425 9.42783,19.7659L16.7977,12.5054
                    C16.9395,12.3671 17.0104,12.1595 17.0104,11.9521L17.0107,11.9526Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color.Black),
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _chevronRight24!!
    }

private var _chevronRight24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun ChevronRight24Preview() {
    Icon(
        imageVector = ChevronRight24,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.outline
    )
}
