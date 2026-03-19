package ru.mercury.vpclient.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.core.ktx.productsQuantityWithThousandsSeparator
import ru.mercury.vpclient.core.ktx.requireProductsQuantity
import ru.mercury.vpclient.core.persistence.database.entity.CatalogFilterProductsQuantityEntity
import ru.mercury.vpclient.core.ui.preview.FilterProductsQuantityEntityProvider
import ru.mercury.vpclient.core.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.core.ui.theme.ClientStrings
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.regular15
import ru.mercury.vpclient.core.ui.theme.secondary6

@Composable
fun FilterProductsQuantityText(
    entity: CatalogFilterProductsQuantityEntity,
    modifier: Modifier = Modifier
) {
    Text(
        text = pluralStringResource(ClientStrings.FilterProductsQuantity, entity.requireProductsQuantity, entity.productsQuantityWithThousandsSeparator),
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.regular15.copy(color = MaterialTheme.colorScheme.secondary6, lineHeight = 19.sp, letterSpacing = .2.sp, textAlign = TextAlign.Center)
    )
}

@FontScalePreviews
@Composable
private fun FilterProductsQuantityTextPreview(
    @PreviewParameter(FilterProductsQuantityEntityProvider::class) entity: CatalogFilterProductsQuantityEntity
) {
    ClientTheme {
        FilterProductsQuantityText(
            entity = entity
        )
    }
}
