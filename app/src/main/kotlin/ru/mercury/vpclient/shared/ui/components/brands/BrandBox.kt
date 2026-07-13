package ru.mercury.vpclient.shared.ui.components.brands

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.livretMedium21

@Composable
fun BrandBox(
    entity: BrandEntity,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.livretMedium21
) {
    val context = LocalContext.current
    var isLogoFailed by remember(entity.urlBrandLogo) { mutableStateOf(false) }

    LaunchedEffect(entity.urlBrandLogo) {
        isLogoFailed = false
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (entity.urlBrandLogo.isNullOrEmpty() || isLogoFailed) {
            Text(
                text = entity.brand,
                style = style.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            )
        } else {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(entity.urlBrandLogo)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Fit,
                onError = {
                    isLogoFailed = true
                },
                onSuccess = {
                    isLogoFailed = false
                }
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun BrandBoxPreview(
    @PreviewParameter(BrandBoxBrandEntityProvider::class) entity: BrandEntity
) {
    BrandBox(
        entity = entity
    )
}

private class BrandBoxBrandEntityProvider: PreviewParameterProvider<BrandEntity> {
    override val values: Sequence<BrandEntity> = sequenceOf(
        BrandEntity(
            brand = "SAINT LAURENT",
            urlBrandLogo = null
        ),
        BrandEntity(
            brand = "GUCCI",
            urlBrandLogo = "https://example.com/brand-logo.png"
        )
    )
}
