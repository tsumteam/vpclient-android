package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.ui.preview.CartProductProvider
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

@Composable
fun CartAlternativesSection(
    products: List<CartProduct>,
    modifier: Modifier = Modifier,
    onProductClick: (CartProduct) -> Unit = {},
    onRemoveClick: (CartProduct) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        CartAlternativesTitle()

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        LazyRow {
            itemsIndexed(
                items = products
            ) { index, product ->
                CartAlternativeProductCard(
                    product = product,
                    isStartBorderVisible = index == 0,
                    onClick = { onProductClick(product) },
                    onRemoveClick = { onRemoveClick(product) }
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartAlternativesSectionPreview(
    @PreviewParameter(CartProductProvider::class) product: CartProduct
) {
    CartAlternativesSection(
        products = listOf(
            product,
            product.copy(id = "2", brand = "DOLCE&GABBANA", price = "1 900 000 ₽"),
            product.copy(id = "3", brand = "MVST", price = "800 000 ₽")
        )
    )
}
