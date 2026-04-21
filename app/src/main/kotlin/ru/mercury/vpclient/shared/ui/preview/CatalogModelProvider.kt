package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.data.entity.CatalogData
import ru.mercury.vpclient.shared.data.entity.CatalogTabData
import ru.mercury.vpclient.features.main.tabs.catalog.stack.catalog.model.CatalogModel

class CatalogModelProvider: PreviewParameterProvider<CatalogModel> {
    override val values: Sequence<CatalogModel> = sequenceOf(
        CatalogModel(),
        CatalogModel(
            catalogData = CatalogData(
                tabs = listOf(
                    CatalogTabData(title = "Мужская", rootId = 3, selected = true),
                    CatalogTabData(title = "Женская", rootId = 2, selected = false),
                    CatalogTabData(title = "Детская", rootId = 4, selected = false)
                ),
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
