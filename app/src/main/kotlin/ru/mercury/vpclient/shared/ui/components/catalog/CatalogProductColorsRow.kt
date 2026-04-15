package ru.mercury.vpclient.shared.ui.components.catalog

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.FORMAT_PLUS
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.preview.CatalogProductColorsRowProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.regular12
import java.util.Locale

@Composable
fun CatalogProductColorsRow(
    colorPhotoUrls: List<String>,
    modifier: Modifier = Modifier
) {
    val visibleUrls = colorPhotoUrls.take(3)
    val extraCount = colorPhotoUrls.size - visibleUrls.size

    Layout(
        modifier = modifier.height(16.dp),
        content = {
            visibleUrls.forEach { url ->
                ClientAsyncImage(
                    imageUrl = url,
                    modifier = Modifier.clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            if (extraCount > 0) {
                Text(
                    text = String.format(Locale.getDefault(), FORMAT_PLUS, extraCount),
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

@FontScalePreviews
@Composable
private fun CatalogProductColorsRowPreview(
    @PreviewParameter(CatalogProductColorsRowProvider::class) colorPhotoUrls: List<String>
) {
    ClientTheme {
        CatalogProductColorsRow(
            colorPhotoUrls = colorPhotoUrls
        )
    }
}
