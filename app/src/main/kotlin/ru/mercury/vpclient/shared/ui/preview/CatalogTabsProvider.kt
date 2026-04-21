package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.data.entity.CatalogTabData

class CatalogTabsProvider: PreviewParameterProvider<List<CatalogTabData>> {
    override val values: Sequence<List<CatalogTabData>> = sequenceOf(
        listOf(
            CatalogTabData(title = "Женское", rootId = 2, selected = true),
            CatalogTabData(title = "Мужское", rootId = 3, selected = false),
            CatalogTabData(title = "Детское", rootId = 4, selected = false)
        ),
        emptyList()
    )
}
