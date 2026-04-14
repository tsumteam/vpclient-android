package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.persistence.database.entity.CatalogFilterProductsQuantityEntity

class FilterProductsQuantityEntityProvider: PreviewParameterProvider<CatalogFilterProductsQuantityEntity> {
    override val values: Sequence<CatalogFilterProductsQuantityEntity> = sequenceOf(
        CatalogFilterProductsQuantityEntity(
            categoryId = 1,
            titleCategoryId = 1,
            productsQuantity = 5717
        )
    )
}
