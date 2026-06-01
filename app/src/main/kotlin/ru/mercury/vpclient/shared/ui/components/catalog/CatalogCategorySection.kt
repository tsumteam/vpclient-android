package ru.mercury.vpclient.shared.ui.components.catalog

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
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.network.response.CatalogCategoryResponse
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.data.persistence.database.pojo.SubcategoryPojo
import ru.mercury.vpclient.shared.domain.mapper.isEmpty
import ru.mercury.vpclient.shared.domain.mapper.isNotEmpty
import ru.mercury.vpclient.shared.ui.PlaceholderHighlight
import ru.mercury.vpclient.shared.ui.placeholder
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.shimmer
import ru.mercury.vpclient.shared.ui.theme.livretMedium18

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
                    style = MaterialTheme.typography.livretMedium18.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 26.sp,
                        letterSpacing = .2.sp,
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

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun CatalogCategorySectionPreview(
    @PreviewParameter(SubcategoryPojoProvider::class) pojo: SubcategoryPojo
) {
    CatalogCategorySection(
        pojo = pojo,
        onClick = {},
        onItemClick = {}
    )
}

private class SubcategoryPojoProvider: PreviewParameterProvider<SubcategoryPojo> {
    private val catalogCategoryEntity = CatalogCategorySectionCatalogCategoryEntityProvider().values.first()
    private val childCategoryEntity = CatalogCategorySectionCatalogCategoryEntityProvider2().values.first()

    override val values: Sequence<SubcategoryPojo> = sequenceOf(
        SubcategoryPojo(
            entity = catalogCategoryEntity.copy(
                level = CatalogCategoryEntity.LEVEL_BOTTOM,
                name = "КУРТКИ"
            ),
            children = listOf(
                childCategoryEntity,
                childCategoryEntity.copy(
                    id = 2,
                    name = "БОМБЕРЫ",
                    position = 2
                ),
                childCategoryEntity.copy(
                    id = 3,
                    name = "ПАЛЬТО",
                    position = 3
                )
            )
        ),
        SubcategoryPojo(
            entity = CatalogCategoryEntity.Empty,
            children = emptyList()
        )
    )
}

private class CatalogCategorySectionCatalogCategoryEntityProvider: PreviewParameterProvider<CatalogCategoryEntity> {
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

private class CatalogCategorySectionCatalogCategoryEntityProvider2: PreviewParameterProvider<CatalogCategoryEntity> {
    override val values: Sequence<CatalogCategoryEntity> = sequenceOf(
        CatalogCategoryEntity(
            id = 1,
            parentId = 10,
            rootId = 2,
            level = CatalogCategoryEntity.LEVEL_BOTTOM,
            name = "ПУХОВЫЕ",
            photoUrl = "",
            categoryType = "",
            sortSettingId = 0,
            position = 1
        )
    )
}
