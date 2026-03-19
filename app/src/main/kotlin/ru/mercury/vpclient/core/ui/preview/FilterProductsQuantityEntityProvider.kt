package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.persistence.database.entity.CatalogFilterProductsQuantityEntity

class FilterProductsQuantityEntityProvider: PreviewParameterProvider<CatalogFilterProductsQuantityEntity> {
    override val values: Sequence<CatalogFilterProductsQuantityEntity> = sequenceOf(
        CatalogFilterProductsQuantityEntity(
            categoryId = 1,
            titleCategoryId = 1,
            productsQuantity = 5717
        )
    )
}
