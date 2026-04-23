package ru.mercury.vpclient.shared.ui.components.system

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.mercury.vpclient.shared.ui.theme.surface4

@Composable
fun ClientAsyncImage(
    imageUrl: String,
    modifier: Modifier,
    contentScale: ContentScale,
    alignment: Alignment = Alignment.Center
) {
    val context = LocalContext.current
    val painter = ColorPainter(MaterialTheme.colorScheme.surface4)

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale,
        alignment = alignment,
        placeholder = painter,
        error = painter,
        fallback = painter
    )
}
