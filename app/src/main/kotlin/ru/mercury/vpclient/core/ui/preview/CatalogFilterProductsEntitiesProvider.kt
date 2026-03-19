package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.persistence.database.entity.CatalogFilterProductsEntity

class CatalogFilterProductsEntitiesProvider: PreviewParameterProvider<List<CatalogFilterProductsEntity>> {
    override val values: Sequence<List<CatalogFilterProductsEntity>> = sequenceOf(
        listOf(
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
            ),
            CatalogFilterProductsEntity(
                categoryId = 1,
                titleCategoryId = 11,
                position = 2,
                id = "preview-3",
                itemId = "item-3",
                colorId = "blue",
                name = "Джинсы прямого кроя",
                price = 74_500.0,
                priceWithoutDiscount = 96_400.0,
                brand = "TOM FORD",
                urlBrandLogo = null,
                imageUrl = "",
                imageUrls = emptyList()
            ),
            CatalogFilterProductsEntity(
                categoryId = 1,
                titleCategoryId = 11,
                position = 3,
                id = "preview-4",
                itemId = "item-4",
                colorId = "beige",
                name = "Кашемировый кардиган на пуговицах",
                price = 128_000.0,
                priceWithoutDiscount = 156_800.0,
                brand = "LORO PIANA",
                urlBrandLogo = null,
                imageUrl = "",
                imageUrls = listOf("", "", "")
            )
        ),
        listOf(
            CatalogFilterProductsEntity.Empty,
            CatalogFilterProductsEntity.Empty,
            CatalogFilterProductsEntity.Empty,
            CatalogFilterProductsEntity.Empty
        )
    )
}
