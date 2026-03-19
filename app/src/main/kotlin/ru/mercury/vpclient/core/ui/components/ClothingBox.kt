package ru.mercury.vpclient.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import ru.mercury.vpclient.core.ktx.isEmpty
import ru.mercury.vpclient.core.ktx.isNotEmpty
import ru.mercury.vpclient.core.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.core.ui.PlaceholderHighlight
import ru.mercury.vpclient.core.ui.shimmer
import ru.mercury.vpclient.core.ui.placeholder
import ru.mercury.vpclient.core.ui.preview.CatalogCategoryEntityProvider
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.black1
import ru.mercury.vpclient.core.ui.theme.livretMedium13
import ru.mercury.vpclient.core.ui.theme.secondary5

@Composable
fun ClothingBox(
    entity: CatalogCategoryEntity,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .size(width = 110.dp, height = 163.dp)
            .clip(RoundedCornerShape(4.dp))
            .clickable(enabled = entity.isNotEmpty, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            entity.isEmpty -> {
                Box(
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(width = 102.dp, height = 114.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.secondary5)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                            shape = RoundedCornerShape(4.dp)
                        )
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, top = 8.dp, end = 4.dp, bottom = 4.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.secondary5)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }
            else -> {
                ClientAsyncImage(
                    imageUrl = entity.photoUrl,
                    modifier = Modifier
                        .padding(top = 4.dp)
                        .size(width = 102.dp, height = 114.dp),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = entity.name.uppercase(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, top = 8.dp, end = 4.dp, bottom = 4.dp),
                    color = MaterialTheme.colorScheme.black1,
                    maxLines = 2,
                    style = MaterialTheme.typography.livretMedium13.copy(textAlign = TextAlign.Center)
                )
            }
        }
    }
}

@FontScalePreviews
@Composable
private fun ClothingBoxPreview(
    @PreviewParameter(CatalogCategoryEntityProvider::class) entity: CatalogCategoryEntity
) {
    ClientTheme {
        ClothingBox(
            entity = entity,
            modifier = Modifier.padding(16.dp)
        )
    }
}
