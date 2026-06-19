package ru.mercury.vpclient.shared.ui.components.catalog

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.FORMAT_PLUS
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular12

@Composable
fun CatalogProductColorsRow(
    colorPhotoUrls: List<String>,
    modifier: Modifier = Modifier
) {
    val visibleUrls = colorPhotoUrls.take(3)
    val extraCount = colorPhotoUrls.size - visibleUrls.size
    val locale = LocalConfiguration.current.locales[0]

    Layout(
        modifier = modifier.height(16.dp),
        content = {
            visibleUrls.forEach { url ->
                CatalogProductColorImage(
                    imageUrl = url,
                    modifier = Modifier.clip(CircleShape)
                )
            }
            if (extraCount > 0) {
                Text(
                    text = String.format(locale, FORMAT_PLUS, extraCount), // fixme
                    style = MaterialTheme.typography.regular12.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = .2.sp
                    )
                )
            }
        }
    ) { measurables, constraints ->
        val circleSizePx = 16.dp.roundToPx()
        val stepPx = 8.dp.roundToPx()
        val extraSpacingPx = 2.dp.roundToPx()
        val circlesCount = visibleUrls.size
        val circleConstraints = Constraints.fixed(circleSizePx, circleSizePx)
        val circlePlaceables = measurables.take(circlesCount).map { it.measure(circleConstraints) }
        val extraPlaceable = if (extraCount > 0 && measurables.size > circlesCount) {
            measurables.last().measure(constraints.copy(minWidth = 0, minHeight = 0))
        } else null
        val totalWidth = if (circlesCount == 0) 0 else {
            val circlesWidth = (circlesCount - 1) * stepPx + circleSizePx
            if (extraPlaceable != null) circlesWidth + extraSpacingPx + extraPlaceable.width
            else circlesWidth
        }
        layout(totalWidth, circleSizePx) {
            circlePlaceables.indices.reversed().forEach { index ->
                circlePlaceables[index].placeRelative(x = index * stepPx, y = 0)
            }
            extraPlaceable?.placeRelative(
                x = (circlesCount - 1) * stepPx + circleSizePx + extraSpacingPx,
                y = (circleSizePx - extraPlaceable.height) / 2
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CatalogProductColorsRowPreview(
    @PreviewParameter(CatalogProductColorsRowProvider::class) colorPhotoUrls: List<String>
) {
    CatalogProductColorsRow(
        colorPhotoUrls = colorPhotoUrls
    )
}

private class CatalogProductColorsRowProvider: PreviewParameterProvider<List<String>> {
    override val values: Sequence<List<String>> = sequenceOf(
        listOf(
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Blue.png"
        ),
        listOf(
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Blue.png",
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Bordo.png"
        ),
        listOf(
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Blue.png",
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Bordo.png",
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Black.png"
        ),
        listOf(
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Blue.png",
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Bordo.png",
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Black.png",
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Grey.png",
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Green.png",
            "https://st.vip-platinum.ru/catalog/ColorSearchCard/Blue.png"
        )
    )
}
