package ru.mercury.vpclient.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.medium16

@Composable
fun BrandBox(
    brand: String,
    urlBrandLogo: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.height(33.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            urlBrandLogo.isNullOrEmpty() -> {
                Text(
                    text = brand,
                    style = MaterialTheme.typography.medium16.copy(color = MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.Center) // fixme Какой тут шрифт?
                )
            }
            else -> {
                AsyncImage(
                    model = urlBrandLogo,
                    contentDescription = brand,
                    modifier = Modifier
                        .height(33.dp)
                        .wrapContentWidth(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@FontScalePreviews
@Composable
private fun BrandBoxTextPreview() {
    ClientTheme {
        BrandBox(
            brand = "SAINT LAURENT",
            urlBrandLogo = null
        )
    }
}
