package ru.mercury.vpclient.shared.ui.components.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.shared.domain.mapper.cardDiscountedPrice
import ru.mercury.vpclient.shared.domain.mapper.cardOldPrice
import ru.mercury.vpclient.shared.domain.mapper.cardPrice
import ru.mercury.vpclient.shared.domain.mapper.detailsMessageProductArticle
import ru.mercury.vpclient.shared.domain.mapper.detailsMessageProductColorName
import ru.mercury.vpclient.shared.domain.mapper.detailsMessageProductName
import ru.mercury.vpclient.shared.domain.mapper.isDiscountPriceVisible
import ru.mercury.vpclient.shared.ui.components.product.ProductBrandBox
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun DetailsMessageProductCard(
    entity: ProductEntity,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        ClientAsyncImage(
            imageUrl = entity.colorImageUrls.firstOrNull().orEmpty(),
            modifier = Modifier
                .padding(start = 16.dp)
                .size(85.dp, 130.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProductBrandBox(
                    entity = BrandEntity(
                        brand = entity.brand.orEmpty(),
                        urlBrandLogo = entity.urlBrandLogo
                    ),
                    modifier = Modifier
                        .weight(1F)
                        .height(24.dp)
                )

                Column(
                    modifier = Modifier
                        .weight(1F)
                        .padding(top = 4.dp),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    when {
                        entity.price == 0.0 -> {
                            Text(
                                text = stringResource(ClientStrings.DetailsSold),
                                style = MaterialTheme.typography.regular14.copy(
                                    color = MaterialTheme.colorScheme.error,
                                    letterSpacing = .2.sp,
                                    textAlign = TextAlign.End
                                )
                            )
                        }
                        entity.isDiscountPriceVisible -> {
                            Text(
                                text = entity.cardOldPrice.orEmpty(),
                                style = MaterialTheme.typography.regular14.copy(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    letterSpacing = .2.sp,
                                    textAlign = TextAlign.End,
                                    textDecoration = TextDecoration.LineThrough
                                )
                            )

                            Text(
                                text = entity.cardDiscountedPrice.orEmpty(),
                                style = MaterialTheme.typography.regular14.copy(
                                    color = MaterialTheme.colorScheme.error,
                                    letterSpacing = .2.sp,
                                    textAlign = TextAlign.End
                                )
                            )
                        }
                        else -> {
                            Text(
                                text = entity.cardPrice,
                                style = MaterialTheme.typography.regular14.copy(
                                    color = MaterialTheme.colorScheme.onBackground,
                                    letterSpacing = .2.sp,
                                    textAlign = TextAlign.End
                                )
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = entity.detailsMessageProductName,
                    modifier = Modifier.weight(1F),
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 18.sp,
                        letterSpacing = .2.sp
                    )
                )

                Spacer(modifier = Modifier.weight(1F))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 9.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(ClientStrings.DetailsMessageProductArticle, entity.detailsMessageProductArticle),
                    modifier = Modifier.weight(1F),
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 18.sp,
                        letterSpacing = .2.sp
                    )
                )

                Text(
                    text = entity.detailsMessageProductColorName,
                    modifier = Modifier.weight(1F),
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        lineHeight = 18.sp,
                        letterSpacing = .2.sp,
                        textAlign = TextAlign.End
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun DetailsMessageProductCardPreview(
    @PreviewParameter(DetailsMessageProductCardProductEntityProvider::class) productEntity: ProductEntity
) {
    DetailsMessageProductCard(
        entity = productEntity
    )
}

private class DetailsMessageProductCardProductEntityProvider: PreviewParameterProvider<ProductEntity> {
    override val values: Sequence<ProductEntity> = sequenceOf(
        ProductEntity.Empty.copy(
            id = "preview-1",
            name = "Кожаная куртка oversize",
            itemId = "000000001",
            brand = "SAINT LAURENT",
            colorName = "Черный",
            shortDescription = "Куртка из кожи oversize",
            price = 189_900.0,
            priceWithoutDiscount = null,
            colorImageUrls = listOf("")
        ),
        ProductEntity.Empty.copy(
            id = "preview-2",
            name = "Хлопковая футболка с логотипом",
            itemId = "",
            brand = "BRUNELLO CUCINELLI",
            colorName = "Молочный",
            article = "M0T18B907",
            shortDescription = "",
            price = 32_700.0,
            priceWithoutDiscount = 45_000.0,
            colorImageUrls = listOf("")
        )
    )
}
