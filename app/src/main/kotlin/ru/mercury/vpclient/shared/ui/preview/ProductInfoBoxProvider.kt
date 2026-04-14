package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.persistence.database.entity.ProductEntity

class ProductInfoBoxProvider: PreviewParameterProvider<ProductEntity> {
    override val values: Sequence<ProductEntity> = sequenceOf(
        ProductEntity.Empty.copy(
            id = "preview-1",
            shortDescription = "Куртка из кожи oversize",
            brand = "SAINT LAURENT",
            price = 189_900.0,
            priceWithoutDiscount = null,
            cashboxActions = emptyList()
        ),
        ProductEntity.Empty.copy(
            id = "preview-2",
            shortDescription = "Хлопковая футболка с логотипом",
            brand = "BRUNELLO CUCINELLI",
            price = 32_700.0,
            priceWithoutDiscount = 45_000.0,
            cashboxActions = listOf("Скидка ЦУМ collect")
        ),
        ProductEntity.Empty.copy(
            id = "preview-3",
            shortDescription = "Шёлковое платье миди",
            brand = "VALENTINO",
            price = 129_900.0,
            priceWithoutDiscount = null,
            cashboxActions = emptyList()
        ),
        ProductEntity.Empty.copy(
            id = "preview-4",
            shortDescription = "Кашемировый джемпер",
            brand = "LORO PIANA",
            price = 74_500.0,
            priceWithoutDiscount = 110_000.0,
            cashboxActions = listOf("Скидка ЦУМ collect", "Скидка по акции")
        )
    )
}
