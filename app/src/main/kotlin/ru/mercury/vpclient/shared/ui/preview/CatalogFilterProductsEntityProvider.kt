package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.persistence.database.entity.CatalogFilterProductsEntity

class CatalogFilterProductsEntityProvider: PreviewParameterProvider<CatalogFilterProductsEntity> {
    override val values: Sequence<CatalogFilterProductsEntity> = sequenceOf(
        CatalogFilterProductsEntity(
            categoryId = 1,
            titleCategoryId = 11,
            position = 0,
            id = "preview-1",
            itemId = "item-1",
            colorId = "black",
            name = "Кожаная куртка oversize",
            price = 189_900.0,
            priceWithoutDiscount = 234_900.0,
            brand = "SAINT LAURENT",
            urlBrandLogo = "https://example.com/brand-logo.png",
            imageUrl = "",
            imageUrls = listOf("", "")
        ),
        CatalogFilterProductsEntity(
            categoryId = 1,
            titleCategoryId = 11,
            position = 1,
            id = "preview-2",
            itemId = "item-2",
            colorId = "white",
            name = "Хлопковая футболка с логотипом",
            price = 32_700.0,
            priceWithoutDiscount = null,
            brand = "BRUNELLO CUCINELLI",
            urlBrandLogo = null,
            imageUrl = "",
            imageUrls = listOf("")
        )
    )
}
