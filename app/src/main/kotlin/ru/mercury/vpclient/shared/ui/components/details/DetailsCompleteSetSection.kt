package ru.mercury.vpclient.shared.ui.components.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.ui.components.catalog.CatalogProductCard
import ru.mercury.vpclient.shared.ui.components.catalog.CatalogProductCardState
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.livretMedium18

@Composable
fun DetailsCompleteSetSection(
    products: List<CatalogFilterProductsEntity>,
    onProductClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    isProductInBasket: (CatalogFilterProductsEntity) -> Boolean = { false },
    onProductMessageClick: (CatalogFilterProductsEntity) -> Unit = {},
    onProductBasketClick: (CatalogFilterProductsEntity) -> Unit = {}
) {
    Column(
        modifier = modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(ClientStrings.DetailsCompleteSetTitle),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp, end = 16.dp),
            style = MaterialTheme.typography.livretMedium18.copy(
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
        )

        Column(
            modifier = Modifier.padding(start = 4.dp, top = 8.dp, end = 4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            products.chunked(2).forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    row.forEach { product ->
                        CatalogProductCard(
                            state = CatalogProductCardState(
                                entity = product,
                                isInBasket = isProductInBasket(product),
                                onClick = { onProductClick(product.id) },
                                onMessageIconClick = { onProductMessageClick(product) },
                                onBasketIconClick = { onProductBasketClick(product) }
                            ),
                            modifier = Modifier.weight(1F)
                        )
                    }
                    if (row.size == 1) {
                        Spacer(
                            modifier = Modifier.weight(1F)
                        )
                    }
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun DetailsCompleteSetSectionPreview(
    @PreviewParameter(DetailsCompleteSetSectionCatalogFilterProductsEntitiesProvider::class) products: List<CatalogFilterProductsEntity>
) {
    DetailsCompleteSetSection(
        products = products,
        onProductClick = {}
    )
}

private class DetailsCompleteSetSectionCatalogFilterProductsEntitiesProvider: PreviewParameterProvider<List<CatalogFilterProductsEntity>> {
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
                imageUrls = listOf("", ""),
                additionalColorPhotoUrls = emptyList()
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
                imageUrls = listOf(""),
                additionalColorPhotoUrls = emptyList()
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
                imageUrls = emptyList(),
                additionalColorPhotoUrls = emptyList()
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
                imageUrls = listOf("", "", ""),
                additionalColorPhotoUrls = emptyList()
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
