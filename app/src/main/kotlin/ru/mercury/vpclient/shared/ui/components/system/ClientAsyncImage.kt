package ru.mercury.vpclient.shared.ui.components.system

import android.util.Log
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
fun ClientAsyncImage(
    imageUrl: String,
    contentScale: ContentScale,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentDescription: String? = null
) {
    val context = LocalContext.current
    val painter = ColorPainter(MaterialTheme.colorScheme.surfaceVariant)

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        alignment = alignment,
        placeholder = painter,
        error = painter,
        fallback = painter,
        onError = { state ->
            Log.e("2", "imageUrl = $imageUrl, state = ${state.result.throwable}")
        }
    )
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun ClientAsyncImagePreview() {
    ClientAsyncImage(
        imageUrl = "",
        modifier = Modifier.size(
            width = 121.dp,
            height = 45.dp
        ),
        contentScale = ContentScale.Fit
    )
}
