package ru.mercury.vpclient.core.ui.components.catalog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.ktx.isEmpty
import ru.mercury.vpclient.core.ktx.isNotEmpty
import ru.mercury.vpclient.core.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.core.persistence.database.pojo.SubcategoryPojo
import ru.mercury.vpclient.core.ui.PlaceholderHighlight
import ru.mercury.vpclient.core.ui.placeholder
import ru.mercury.vpclient.core.ui.preview.SubcategoryPojoProvider
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.shimmer
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.livretMedium19

@Composable
fun CatalogCategorySection(
    pojo: SubcategoryPojo,
    onClick: () -> Unit,
    onItemClick: (CatalogCategoryEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(208.dp)
            .clickable(enabled = pojo.entity.isNotEmpty, onClick = onClick)
    ) {
        when {
            pojo.entity.isEmpty -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 64.dp, top = 9.dp, end = 64.dp)
                        .height(24.dp)
                        .placeholder(
                            visible = true,
                            highlight = PlaceholderHighlight.shimmer(),
                            shape = RoundedCornerShape(4.dp)
                        )
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, bottom = 5.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    userScrollEnabled = false
                ) {
                    items(count = 4) {
                        CatalogSubcategoryCard(
                            entity = CatalogCategoryEntity.Empty
                        )
                    }
                }
            }
            else -> {
                Text(
                    text = pojo.entity.name.uppercase(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 9.dp, end = 16.dp),
                    style = MaterialTheme.typography.livretMedium19.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 11.dp, bottom = 1.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        items = pojo.children
                    ) { entity ->
                        CatalogSubcategoryCard(
                            entity = entity,
                            onClick = { onItemClick(entity) }
                        )
                    }

                    item {
                        Box(
                            modifier = Modifier.height(155.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CatalogViewAllButton(
                                onClick = onClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@FontScalePreviews
@Composable
private fun CatalogCategorySectionPreview(
    @PreviewParameter(SubcategoryPojoProvider::class) pojo: SubcategoryPojo
) {
    ClientTheme {
        CatalogCategorySection(
            pojo = pojo,
            onClick = {},
            onItemClick = {}
        )
    }
}
