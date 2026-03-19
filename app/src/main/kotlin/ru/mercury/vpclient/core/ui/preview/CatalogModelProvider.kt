package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.entity.CatalogData
import ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.model.CatalogModel

class CatalogModelProvider: PreviewParameterProvider<CatalogModel> {
    override val values: Sequence<CatalogModel> = sequenceOf(
        CatalogModel(),
        CatalogModel(
            catalogData = CatalogData(
                tabs = listOf("Мужская", "Женская", "Детская"),
                pages = listOf(
                    listOf(
                        CatalogCategoryEntityProvider().values.first(),
                        CatalogCategoryEntityProvider().values.first().copy(
                            id = 11,
                            parentId = 3,
                            rootId = 3,
                            name = "Аксессуары",
                            position = 2
                        )
                    )
                )
            )
        )
    )
}
