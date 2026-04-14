package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.entity.FilterTitleEntity

class FilterTitleEntityProvider: PreviewParameterProvider<FilterTitleEntity> {
    private val titleCatalogCategoryEntity = CatalogCategoryEntityProvider().values.first()
    private val subtitleCatalogCategoryEntity = CatalogCategoryEntityProvider2().values.first()

    override val values: Sequence<FilterTitleEntity> = sequenceOf(
        FilterTitleEntity.Empty,
        FilterTitleEntity(
            titleCatalogCategoryEntity = titleCatalogCategoryEntity,
            subtitleCatalogCategoryEntity = subtitleCatalogCategoryEntity
        )
    )
}
