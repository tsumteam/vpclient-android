package ru.mercury.vpclient.core.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.mercury.vpclient.core.ui.theme.surface4

@Composable
fun ClientAsyncImage(
    imageUrl: String,
    modifier: Modifier,
    contentScale: ContentScale
) {
    val context = LocalContext.current

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale,
        placeholder = ColorPainter(MaterialTheme.colorScheme.surface4),
        error = ColorPainter(MaterialTheme.colorScheme.surface4),
        fallback = ColorPainter(MaterialTheme.colorScheme.surface4)
    )
}
