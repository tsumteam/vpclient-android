package ru.mercury.vpclient.shared.ui.components.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.network.type.CatalogCategoryType
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.domain.mapper.isEmpty
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.livretMedium19

@Composable
fun CatalogClothingCard(
    entity: CatalogCategoryEntity,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(149.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .placeholder(visible = entity.isEmpty)
    ) {
        ClientAsyncImage(
            imageUrl = entity.photoUrl,
            modifier = Modifier
                .fillMaxWidth()
                .height(149.dp),
            contentScale = ContentScale.Crop
        )

        Text(
            text = entity.name.uppercase(),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            style = MaterialTheme.typography.livretMedium19.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CatalogClothingCardPreview(
    @PreviewParameter(CatalogClothingCardCatalogCategoryEntityProvider::class) entity: CatalogCategoryEntity
) {
    CatalogClothingCard(
        entity = entity
    )
}

private class CatalogClothingCardCatalogCategoryEntityProvider: PreviewParameterProvider<CatalogCategoryEntity> {
    override val values: Sequence<CatalogCategoryEntity> = sequenceOf(
        CatalogCategoryEntity(
            id = 10,
            parentId = 2,
            rootId = 2,
            level = CatalogCategoryEntity.LEVEL_TOP,
            name = "Хрусталь и коллекции для дома",
            photoUrl = "",
            categoryType = CatalogCategoryType.CATALOG,
            sortSettingId = 0,
            position = 1
        ),
        CatalogCategoryEntity.Empty
    )
}
