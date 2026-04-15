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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.domain.mapper.isEmpty
import ru.mercury.vpclient.shared.domain.mapper.isNotEmpty
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.CatalogCategoryEntityProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.black1
import ru.mercury.vpclient.shared.ui.theme.livretMedium13

@Composable
fun CatalogSubcategoryCard(
    entity: CatalogCategoryEntity,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .size(width = 110.dp, height = 163.dp)
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
            color = MaterialTheme.colorScheme.black1,
            maxLines = 2,
            style = MaterialTheme.typography.livretMedium13.copy(
                textAlign = TextAlign.Center
            )
        )
    }
}

@FontScalePreviews
@Composable
private fun CatalogSubcategoryCardPreview(
    @PreviewParameter(CatalogCategoryEntityProvider::class) entity: CatalogCategoryEntity
) {
    ClientTheme {
        CatalogSubcategoryCard(
            entity = entity,
            modifier = Modifier.padding(16.dp)
        )
    }
}
