package ru.mercury.vpclient.shared.ui.components.details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.shared.domain.mapper.cardDiscountLabel
import ru.mercury.vpclient.shared.domain.mapper.isDiscountLabelVisible
import ru.mercury.vpclient.shared.ui.components.brands.BrandBox
import ru.mercury.vpclient.shared.ui.components.DiscountBadge
import ru.mercury.vpclient.shared.ui.components.PriceText
import ru.mercury.vpclient.shared.ui.icons.Message24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.regular14

data class DetailsProductInfoBoxState(
    val productEntity: ProductEntity,
    val availabilityText: Int? = null,
    val onMessageClick: () -> Unit = {}
)

@Composable
fun DetailsProductInfoBox(
    state: DetailsProductInfoBoxState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(bottom = 12.dp)
    ) {
        IconButton(
            onClick = state.onMessageClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 8.dp, top = 31.dp)
        ) {
            Icon(
                imageVector = Message24,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
        }

        BrandBox(
            entity = BrandEntity(
                brand = state.productEntity.brand.orEmpty(),
                urlBrandLogo = state.productEntity.urlBrandLogo
            ),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(width = 180.dp, height = 50.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 64.dp, top = 54.dp, end = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = state.productEntity.shortDescription.orEmpty(),
                modifier = Modifier.fillMaxWidth(),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                style = MaterialTheme.typography.regular14.copy(color = MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.Center)
            )

            state.productEntity.cashboxActions.forEach { action ->
                Text(
                    text = action,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.regular14.copy(color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center)
                )
            }

            PriceText(
                entity = state.productEntity,
                modifier = Modifier.padding(top = 6.dp)
            )

            state.availabilityText?.let { availabilityText ->
                Text(
                    text = stringResource(availabilityText),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                )
            }

            if (state.productEntity.isDiscountLabelVisible) {
                DiscountBadge(
                    percentText = state.productEntity.cardDiscountLabel.orEmpty(),
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun DetailsProductInfoBoxPreview(
    @PreviewParameter(ProductInfoBoxStateProvider::class) state: DetailsProductInfoBoxState
) {
    DetailsProductInfoBox(
        state = state,
        modifier = Modifier.fillMaxWidth()
    )
}

private class ProductInfoBoxStateProvider: PreviewParameterProvider<DetailsProductInfoBoxState> {
    override val values: Sequence<DetailsProductInfoBoxState> = sequenceOf(
        DetailsProductInfoBoxState(
            productEntity = ProductEntity.Empty.copy(
                id = "preview-1",
                shortDescription = "Куртка из кожи oversize",
                brand = "SAINT LAURENT",
                price = 189_900.0,
                priceWithoutDiscount = null,
                cashboxActions = emptyList()
            )
        ),
        DetailsProductInfoBoxState(
            productEntity = ProductEntity.Empty.copy(
                id = "preview-2",
                shortDescription = "Хлопковая футболка с логотипом",
                brand = "BRUNELLO CUCINELLI",
                price = 32_700.0,
                priceWithoutDiscount = 45_000.0,
                cashboxActions = listOf("Скидка ЦУМ collect")
            )
        ),
        DetailsProductInfoBoxState(
            productEntity = ProductEntity.Empty.copy(
                id = "preview-3",
                shortDescription = "Шёлковое платье миди",
                brand = "VALENTINO",
                price = 129_900.0,
                priceWithoutDiscount = null,
                cashboxActions = emptyList()
            ),
            availabilityText = ClientStrings.DetailsSold
        ),
        DetailsProductInfoBoxState(
            productEntity = ProductEntity.Empty.copy(
                id = "preview-5",
                shortDescription = "Сумка из кожи",
                brand = "BOTTEGA VENETA",
                price = 219_900.0,
                priceWithoutDiscount = null,
                cashboxActions = emptyList()
            ),
            availabilityText = ClientStrings.DetailsSizeInStock
        ),
        DetailsProductInfoBoxState(
            productEntity = ProductEntity.Empty.copy(
                id = "preview-4",
                shortDescription = "Кашемировый джемпер",
                brand = "LORO PIANA",
                price = 74_500.0,
                priceWithoutDiscount = 110_000.0,
                cashboxActions = listOf("Скидка ЦУМ collect", "Скидка по акции")
            )
        )
    )
}
