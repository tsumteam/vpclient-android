package ru.mercury.vpclient.shared.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.PREFIX_SPACE
import ru.mercury.vpclient.shared.data.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.data.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.shared.domain.mapper.cardDiscountedPrice
import ru.mercury.vpclient.shared.domain.mapper.cardOldPrice
import ru.mercury.vpclient.shared.domain.mapper.cardPrice
import ru.mercury.vpclient.shared.domain.mapper.isDiscountPriceVisible
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun PriceText(
    entity: CatalogFilterProductsEntity,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    PriceText(
        price = entity.cardPrice,
        isSold = entity.price == 0.0,
        oldPrice = entity.cardOldPrice,
        discountedPrice = entity.cardDiscountedPrice,
        isDiscountPriceVisible = entity.isDiscountPriceVisible,
        modifier = modifier,
        textAlign = textAlign
    )
}

@Composable
fun PriceText(
    entity: ProductEntity,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    PriceText(
        price = entity.cardPrice,
        isSold = entity.price == 0.0,
        oldPrice = entity.cardOldPrice,
        discountedPrice = entity.cardDiscountedPrice,
        isDiscountPriceVisible = entity.isDiscountPriceVisible,
        modifier = modifier,
        textAlign = textAlign
    )
}

@Composable
private fun PriceText(
    price: String,
    isSold: Boolean,
    oldPrice: String?,
    discountedPrice: String?,
    isDiscountPriceVisible: Boolean,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    when {
        isSold -> {
            Text(
                text = stringResource(ClientStrings.DetailsSold),
                modifier = modifier,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.error,
                    letterSpacing = .2.sp,
                    textAlign = textAlign
                )
            )
        }
        isDiscountPriceVisible -> {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            textDecoration = TextDecoration.LineThrough
                        )
                    ) { append(oldPrice.orEmpty()) }
                    append(PREFIX_SPACE)
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.error
                        )
                    ) { append(discountedPrice.orEmpty()) }
                },
                modifier = modifier,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = .2.sp,
                    textAlign = textAlign
                )
            )
        }
        else -> {
            Text(
                text = price,
                modifier = modifier,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = .2.sp,
                    textAlign = textAlign
                )
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun PriceTextPreview(
    @PreviewParameter(PriceTextCatalogFilterProductsEntityProvider::class) entity: CatalogFilterProductsEntity
) {
    PriceText(
        entity = entity
    )
}

private class PriceTextCatalogFilterProductsEntityProvider: PreviewParameterProvider<CatalogFilterProductsEntity> {
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
            imageUrls = listOf("", ""),
            additionalColorPhotoUrls = listOf(
                "https://st.vip-platinum.ru/catalog/ColorSearchCard/Blue.png",
                "https://st.vip-platinum.ru/catalog/ColorSearchCard/Bordo.png",
                "https://st.vip-platinum.ru/catalog/ColorSearchCard/Black.png",
                "https://st.vip-platinum.ru/catalog/ColorSearchCard/Grey.png",
                "https://st.vip-platinum.ru/catalog/ColorSearchCard/Green.png"
            )
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
            colorId = "red",
            name = "Шелковое платье",
            price = 0.0,
            priceWithoutDiscount = 159_900.0,
            brand = "VALENTINO",
            urlBrandLogo = null,
            imageUrl = "",
            imageUrls = listOf(""),
            additionalColorPhotoUrls = emptyList()
        )
    )
}
