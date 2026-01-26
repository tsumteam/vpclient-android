package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.persistence.database.entity.ProductEntity

class ProductEntityProvider1: PreviewParameterProvider<ProductEntity> {
    override val values: Sequence<ProductEntity> = sequenceOf(
        ProductEntity.Empty.copy(
            itemColorId = "Серый_0940",
            itemId = "6931977",
            itemName = "Брюки",
            itemSizeId = "40",
            price = 643500.0,
            submarkName = "MVST"
        )
    )
}
