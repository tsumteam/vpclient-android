package ru.mercury.vpclient.core.ui.components

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
import ru.mercury.vpclient.core.ktx.cardDiscountedPrice
import ru.mercury.vpclient.core.ktx.cardOldPrice
import ru.mercury.vpclient.core.ktx.cardPrice
import ru.mercury.vpclient.core.ktx.isDiscountPriceVisible
import ru.mercury.vpclient.core.persistence.database.entity.CatalogFilterProductsEntity
import ru.mercury.vpclient.core.ui.preview.CatalogFilterProductsEntityProvider
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.regular14

@Composable
fun PriceText(
    entity: CatalogFilterProductsEntity,
    modifier: Modifier = Modifier
) {
    when {
        entity.isDiscountPriceVisible -> {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground, textDecoration = TextDecoration.LineThrough)) { append(entity.cardOldPrice) }
                    append(" ")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) { append(entity.cardDiscountedPrice) }
                },
                modifier = modifier,
                style = MaterialTheme.typography.regular14.copy(color = MaterialTheme.colorScheme.onBackground, letterSpacing = .2.sp, textAlign = TextAlign.Center)
            )
        }
        else -> {
            Text(
                text = entity.cardPrice,
                modifier = modifier,
                style = MaterialTheme.typography.regular14.copy(color = MaterialTheme.colorScheme.onBackground, letterSpacing = .2.sp, textAlign = TextAlign.Center)
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
