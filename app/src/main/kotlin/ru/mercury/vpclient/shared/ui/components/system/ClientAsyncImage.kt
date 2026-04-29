package ru.mercury.vpclient.shared.ui.components.system

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.mercury.vpclient.R
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.surface4

@Composable
fun ClientAsyncImage(
    imageUrl: String,
    modifier: Modifier,
    contentScale: ContentScale,
    alignment: Alignment = Alignment.Center
) {
    val context = LocalContext.current
    val isInspectionMode = LocalInspectionMode.current
    val painter = ColorPainter(MaterialTheme.colorScheme.surface4)

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(imageUrl)
            .crossfade(true)
            .apply {
                if (isInspectionMode) {
                    placeholder(R.drawable.ic_placeholder)
                }
            }
            .build(),
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale,
        alignment = alignment,
        placeholder = if (isInspectionMode) null else painter,
        error = if (isInspectionMode) null else painter,
        fallback = if (isInspectionMode) null else painter
    )
}

@Preview
@Composable
private fun ClientAsyncImagePreview() {
    ClientTheme {
        ClientAsyncImage(
            imageUrl = "https://example.com/image.png",
            modifier = Modifier.size(
                width = 121.dp,
                height = 45.dp
            ),
            contentScale = ContentScale.Fit
        )
    }
}
