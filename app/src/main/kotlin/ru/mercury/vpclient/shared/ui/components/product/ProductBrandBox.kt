package ru.mercury.vpclient.shared.ui.components.product

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.ui.components.cart.CartAsyncImage
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.livretMedium21

@Composable
fun ProductBrandBox(
    entity: BrandEntity,
    modifier: Modifier = Modifier
) {
    Log.e("2", "entity = $entity")
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        when {
            !entity.urlBrandLogo.isNullOrEmpty() -> {
                CartAsyncImage(
                    imageUrl = entity.urlBrandLogo,
                    modifier = Modifier.matchParentSize()
                )
            }
            entity.brand.isNotEmpty() -> {
                Text(
                    text = entity.brand,
                    style = MaterialTheme.typography.livretMedium21.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Start
                    )
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun ProductBrandBoxPreview(
    @PreviewParameter(ProductBrandBoxBrandEntityProvider::class) entity: BrandEntity
) {
    ProductBrandBox(
        entity = entity
    )
}

private class ProductBrandBoxBrandEntityProvider: PreviewParameterProvider<BrandEntity> {
    override val values: Sequence<BrandEntity> = sequenceOf(
        BrandEntity(
            brand = "SAINT LAURENT",
            urlBrandLogo = null
        ),
        BrandEntity(
            brand = "GUCCI",
            urlBrandLogo = "https://example.com/brand-logo.png"
        )
    )
}
