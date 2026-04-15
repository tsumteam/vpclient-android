package ru.mercury.vpclient.shared.ui.components.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.domain.mapper.isEmpty
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.CatalogCategoryEntityProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.black1
import ru.mercury.vpclient.shared.ui.theme.livretMedium19
import ru.mercury.vpclient.shared.ui.theme.surface4

@Composable
fun CatalogClothingCard(
    entity: CatalogCategoryEntity,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(149.dp)
            .background(MaterialTheme.colorScheme.surface4)
            .placeholder(
                visible = entity.isEmpty,
                highlight = PlaceholderHighlight.shimmer(),
                color = MaterialTheme.colorScheme.surface4
            )
    ) {
        ClientAsyncImage(
            imageUrl = entity.photoUrl,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        Text(
            text = entity.name.uppercase(),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.livretMedium19.copy(
                color = MaterialTheme.colorScheme.black1
            )
        )
    }
}

@FontScalePreviews
@Composable
private fun CatalogClothingCardPreview(
    @PreviewParameter(CatalogCategoryEntityProvider::class) entity: CatalogCategoryEntity
) {
    ClientTheme {
        CatalogClothingCard(
            entity = entity
        )
    }
}
