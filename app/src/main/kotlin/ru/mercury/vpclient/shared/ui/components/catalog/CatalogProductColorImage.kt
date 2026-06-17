package ru.mercury.vpclient.shared.ui.components.catalog

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Precision
import coil.size.Scale

@Composable
fun CatalogProductColorImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val painter = ColorPainter(MaterialTheme.colorScheme.surfaceVariant)
    val requestSizePx = with(density) { 32.dp.roundToPx() }

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .size(requestSizePx, requestSizePx)
            .precision(Precision.EXACT)
            .scale(Scale.FILL)
            .build(),
        contentDescription = null,
        modifier = modifier.size(16.dp),
        contentScale = ContentScale.Crop,
        filterQuality = FilterQuality.High,
        placeholder = painter,
        error = painter,
        fallback = painter
    )
}
