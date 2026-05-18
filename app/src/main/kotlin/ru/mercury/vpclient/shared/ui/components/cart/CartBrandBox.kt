package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.ui.preview.BrandEntityProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.livretMedium21

@Composable
fun CartBrandBox(
    entity: BrandEntity,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        when {
            entity.urlBrandLogo.isNullOrEmpty() -> {
                Text(
                    text = entity.brand,
                    style = MaterialTheme.typography.livretMedium21.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Start
                    )
                )
            }
            else -> {
                CartAsyncImage(
                    imageUrl = entity.urlBrandLogo,
                    modifier = Modifier.matchParentSize()
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun CartBrandBoxPreview(
    @PreviewParameter(BrandEntityProvider::class) entity: BrandEntity
) {
    CartBrandBox(
        entity = entity
    )
}
