package ru.mercury.vpclient.shared.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.preview.BrandEntityProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.livretMedium21

@Composable
fun BrandBox(
    entity: BrandEntity,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.height(33.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            entity.urlBrandLogo.isNullOrEmpty() -> {
                Text(
                    text = entity.brand,
                    style = MaterialTheme.typography.livretMedium21.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                )
            }
            else -> {
                ClientAsyncImage(
                    imageUrl = entity.urlBrandLogo,
                    modifier = Modifier.height(33.dp),
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
            entity = BrandEntity(
                brand = "SAINT LAURENT",
                urlBrandLogo = null
            )
        )
    }
}

@FontScalePreviews
@Composable
private fun BrandBoxPreview(
    @PreviewParameter(BrandEntityProvider::class) entity: BrandEntity
) {
    ClientTheme {
        BrandBox(entity = entity)
    }
}
