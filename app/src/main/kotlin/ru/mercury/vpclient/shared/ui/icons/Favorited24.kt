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

val Favorited24: ImageVector
    get() {
        if (_favorited24 != null) {
            return _favorited24!!
        }
        _favorited24 = ImageVector.Builder(
            name = "Favorited24",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24F,
            viewportHeight = 24F
        ).apply {
            addPath(
                pathData = addPathNodes(
                    """
                    M17.0977,2.5C20.435,2.50003 22.5,5.41099 22.5,7.79004C22.4998,10.4236 20.9888,13.0252 18.835,15.4258
                    C16.783,17.7128 14.2076,19.7486 12,21.377C9.79248,19.7487 7.21701,17.7137 5.16504,15.4268
                    C3.0111,13.0262 1.50016,10.424 1.5,7.79004C1.5,5.41077 3.56488,2.50003 6.90234,2.5
                    C7.78781,2.5 8.63653,2.85086 9.44043,3.41211C10.245,3.97388 10.9763,4.72711 11.6191,5.48242
                    L12,5.92969L12.3809,5.48242C13.0238,4.72703 13.7559,3.9739 14.5605,3.41211
                    C15.3644,2.85097 16.2123,2.5 17.0977,2.5Z
                    """.trimIndent()
                ),
                fill = SolidColor(Color(0xFF1B1B1B)),
                stroke = SolidColor(Color(0xFF1B1B1B)),
                strokeLineWidth = 1F,
                pathFillType = PathFillType.NonZero
            )
        }.build()
        return _favorited24!!
    }

private var _favorited24: ImageVector? = null

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun Favorited24Preview() {
    Icon(
        imageVector = Favorited24,
        contentDescription = null,
        tint = Color.Unspecified
    )
}
