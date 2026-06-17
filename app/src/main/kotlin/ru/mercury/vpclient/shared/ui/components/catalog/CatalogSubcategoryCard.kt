package ru.mercury.vpclient.shared.ui.components.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.network.response.CatalogCategoryResponse
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.domain.mapper.isEmpty
import ru.mercury.vpclient.shared.domain.mapper.isNotEmpty
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.livretMedium10

@Composable
fun CatalogSubcategoryCard(
    entity: CatalogCategoryEntity,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .size(width = 110.dp, height = 153.dp)
            .padding(4.dp)
            .clip(RoundedCornerShape(4.dp))
            .clickable(enabled = entity.isNotEmpty, onClick = onClick),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ClientAsyncImage(
            imageUrl = entity.photoUrl,
            modifier = Modifier
                .size(width = 102.dp, height = 114.dp)
                .placeholder(
                    visible = entity.isEmpty,
                    highlight = PlaceholderHighlight.shimmer(),
                    shape = RoundedCornerShape(4.dp)
                ),
            contentScale = ContentScale.Fit
        )

        Text(
            text = entity.name.uppercase(),
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .placeholder(
                    visible = entity.isEmpty,
                    highlight = PlaceholderHighlight.shimmer(),
                    shape = RoundedCornerShape(4.dp)
                ),
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            style = MaterialTheme.typography.livretMedium10.copy(
                textAlign = TextAlign.Center
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CatalogSubcategoryCardPreview(
    @PreviewParameter(CatalogSubcategoryCardCatalogCategoryEntityProvider::class) entity: CatalogCategoryEntity
) {
    CatalogSubcategoryCard(
        entity = entity,
        modifier = Modifier.padding(16.dp)
    )
}

private class CatalogSubcategoryCardCatalogCategoryEntityProvider: PreviewParameterProvider<CatalogCategoryEntity> {
    override val values: Sequence<CatalogCategoryEntity> = sequenceOf(
        CatalogCategoryEntity(
            id = 10,
            parentId = 2,
            rootId = 2,
            level = CatalogCategoryEntity.LEVEL_TOP,
            name = "Одежда",
            photoUrl = "",
            categoryType = CatalogCategoryResponse.CATEGORY_TYPE_CATALOG,
            sortSettingId = 0,
            position = 1
        ),
        CatalogCategoryEntity.Empty
    )
}
