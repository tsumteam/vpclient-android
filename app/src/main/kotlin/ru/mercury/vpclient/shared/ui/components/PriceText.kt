package ru.mercury.vpclient.shared.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ktx.cardDiscountedPrice
import ru.mercury.vpclient.shared.ktx.cardOldPrice
import ru.mercury.vpclient.shared.ktx.cardPrice
import ru.mercury.vpclient.shared.ktx.isDiscountPriceVisible
import ru.mercury.vpclient.shared.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.shared.persistence.database.entity.ProductEntity
import ru.mercury.vpclient.shared.ui.preview.CatalogFilterProductsEntityProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.regular14

// fixme

@Composable
fun PriceText(
    entity: CatalogFilterProductsEntity,
    modifier: Modifier = Modifier
) {
    PriceText(
        price = entity.cardPrice,
        oldPrice = entity.cardOldPrice,
        discountedPrice = entity.cardDiscountedPrice,
        isDiscountPriceVisible = entity.isDiscountPriceVisible,
        modifier = modifier
    )
}

@Composable
fun PriceText(
    entity: ProductEntity,
    modifier: Modifier = Modifier
) {
    PriceText(
        price = entity.cardPrice,
        oldPrice = entity.cardOldPrice,
        discountedPrice = entity.cardDiscountedPrice,
        isDiscountPriceVisible = entity.isDiscountPriceVisible,
        modifier = modifier
    )
}

@Composable
private fun PriceText(
    price: String,
    oldPrice: String?,
    discountedPrice: String?,
    isDiscountPriceVisible: Boolean,
    modifier: Modifier = Modifier
) {
    when {
        isDiscountPriceVisible -> {
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            textDecoration = TextDecoration.LineThrough
                        )
                    ) { append(oldPrice.orEmpty()) }
                    append(" ")
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
                    textAlign = TextAlign.Center
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
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@FontScalePreviews
@Composable
private fun PriceTextPreview(
    @PreviewParameter(CatalogFilterProductsEntityProvider::class) entity: CatalogFilterProductsEntity
) {
    ClientTheme {
        PriceText(
            entity = entity
        )
    }
}
