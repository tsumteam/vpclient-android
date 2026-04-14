package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.persistence.database.entity.CatalogCategoryEntity

class CatalogCategoryEntityProvider2: PreviewParameterProvider<CatalogCategoryEntity> {
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
