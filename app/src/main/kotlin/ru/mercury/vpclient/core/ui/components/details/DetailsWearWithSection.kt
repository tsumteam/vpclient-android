package ru.mercury.vpclient.core.ui.components.details

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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.core.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.core.ui.components.catalog.CatalogProductCard
import ru.mercury.vpclient.core.ui.preview.CatalogFilterProductsEntitiesProvider
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.livretMedium18

@Composable
fun DetailsWearWithSection(
    products: List<CatalogFilterProductsEntity>,
    modifier: Modifier = Modifier,
    onProductClick: (CatalogFilterProductsEntity) -> Unit = {},
    onProductMessageClick: (CatalogFilterProductsEntity) -> Unit = {},
    onProductBasketClick: (CatalogFilterProductsEntity) -> Unit = {}
) {
    Column(
        modifier = modifier.padding(top = 24.dp)
    ) {
        Text(
            text = stringResource(ClientStrings.DetailsWearWithTitle),
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
                            entity = product,
                            modifier = Modifier.weight(1F),
                            onClick = { onProductClick(product) },
                            onMessageClick = { onProductMessageClick(product) },
                            onBasketClick = { onProductBasketClick(product) }
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

@FontScalePreviews
@Composable
private fun DetailsWearWithSectionPreview(
    @PreviewParameter(CatalogFilterProductsEntitiesProvider::class) products: List<CatalogFilterProductsEntity>
) {
    ClientTheme {
        DetailsWearWithSection(
            products = products
        )
    }
}
