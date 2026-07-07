package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

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

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CartAsyncImagePreview() {
    CartAsyncImage(
        imageUrl = "https://example.com/image.png",
        modifier = Modifier.size(
            width = 121.dp,
            height = 45.dp
        )
    )
}
