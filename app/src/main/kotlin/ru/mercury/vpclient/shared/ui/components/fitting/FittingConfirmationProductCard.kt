package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.ui.components.BrandBox
import ru.mercury.vpclient.shared.ui.components.system.ClientAsyncImage
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular11

@Composable
fun FittingConfirmationProductCard(
    product: CartProduct,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.size(width = 112.dp, height = 149.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ClientAsyncImage(
            imageUrl = product.imageUrl,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .padding(top = 14.dp)
                .size(width = 62.dp, height = 96.dp)
        )

        BrandBox(
            entity = BrandEntity(
                brand = product.brand,
                urlBrandLogo = product.urlBrandLogo
            ),
            modifier = Modifier
                .padding(top = 4.dp)
                .size(width = 96.dp, height = 20.dp),
            style = MaterialTheme.typography.regular11
        )

        Text(
            text = product.price,
            modifier = Modifier
                .padding(top = 2.dp)
                .width(96.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.regular11.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun FittingConfirmationProductCardPreview(
    @PreviewParameter(FittingConfirmationProductCardCartProductProvider::class) product: CartProduct
) {
    FittingConfirmationProductCard(
        product = product
    )
}

private class FittingConfirmationProductCardCartProductProvider: PreviewParameterProvider<CartProduct> {
    override val values: Sequence<CartProduct> = previewCartProducts()
}

private fun previewCartProducts(): Sequence<CartProduct> {
    return sequenceOf(
        CartProduct(
            id = "1",
            detailId = "1",
            itemId = "1",
            colorId = "1",
            brand = "BRUNELLO CUCINELLI",
            urlBrandLogo = null,
            name = "Хлопковая футболка с логотипом",
            article = "MP827743",
            color = "Белый",
            size = "IT 48",
            price = "1 600 000 ₽",
            lookId = "look_1",
            lookName = "Образ",
            lookImageUrl = "",
            imageUrl = "",
            isForPayment = true,
            priceValue = 1_600_000.0
        ),
        CartProduct(
            id = "2",
            detailId = "2",
            itemId = "2",
            colorId = "2",
            brand = "SAINT LAURENT",
            urlBrandLogo = null,
            name = "Кожаная куртка",
            article = "SL908221",
            color = "Черный",
            size = "FR 38",
            price = "300 000 ₽",
            oldPrice = "400 000 ₽",
            lookId = "look_1",
            lookName = "Образ",
            lookImageUrl = "",
            imageUrl = "",
            isForPayment = false,
            quantity = 2,
            priceValue = 300_000.0
        ),
        CartProduct(
            id = "3",
            detailId = "3",
            itemId = "3",
            colorId = "3",
            brand = "LORO PIANA",
            urlBrandLogo = null,
            name = "Кашемировый джемпер",
            article = "LP112490",
            color = "Серый",
            size = "M",
            price = "580 000 ₽",
            imageUrl = "",
            isForPayment = false,
            isSold = true,
            isAlternativesPaletteOpen = true,
            alternatives = listOf(
                CartProductAlternative(
                    id = "1",
                    detailId = "1",
                    brand = "LORO PIANA",
                    urlBrandLogo = null,
                    price = "580 000 ₽",
                    imageUrl = "",
                    isOriginal = true
                ),
                CartProductAlternative(
                    id = "2",
                    detailId = "2",
                    brand = "DOLCE&GABBANA",
                    urlBrandLogo = null,
                    price = "1 900 000 ₽",
                    imageUrl = "",
                    isOriginal = false
                )
            ),
            priceValue = 580_000.0
        ),
        CartProduct(
            id = "4",
            detailId = "4",
            itemId = "4",
            colorId = "4",
            brand = "KITON",
            urlBrandLogo = null,
            name = "Шерстяной жакет",
            article = "KT554210",
            color = "Темно-синий",
            size = "",
            price = "920 000 ₽",
            imageUrl = "",
            isForPayment = false,
            priceValue = 920_000.0
        )
    )
}
