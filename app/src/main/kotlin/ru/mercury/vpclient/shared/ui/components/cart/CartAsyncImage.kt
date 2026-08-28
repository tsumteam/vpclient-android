package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun CartAsyncImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val painter = ColorPainter(MaterialTheme.colorScheme.surfaceVariant)

    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageUrl)
                .build(),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            placeholder = painter,
            error = painter,
            fallback = painter,
            alignment = Alignment.CenterStart,
            contentScale = ContentScale.Fit
        )
    }
}
