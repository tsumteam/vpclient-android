package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.persistence.database.entity.CatalogCategoryEntity
import ru.mercury.vpclient.shared.persistence.database.pojo.SubcategoryPojo

class SubcategoryPojoProvider: PreviewParameterProvider<SubcategoryPojo> {
    private val catalogCategoryEntity = CatalogCategoryEntityProvider().values.first()
    private val childCategoryEntity = CatalogCategoryEntityProvider2().values.first()

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
