package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.data.network.response.CatalogCategoryResponse
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogCategoryEntity

class CatalogCategoryEntityProvider: PreviewParameterProvider<CatalogCategoryEntity> {
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
