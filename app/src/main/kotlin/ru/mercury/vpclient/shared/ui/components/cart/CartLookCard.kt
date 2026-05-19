package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.ui.preview.CartProductProvider
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

@Composable
fun CartLookCard(
    lookName: String,
    lookImageUrl: String?,
    products: List<CartProduct>,
    isLargeCard: Boolean,
    onAddClick: () -> Unit,
    onProductClick: (CartProduct) -> Unit,
    onSelectSizeClick: (CartProduct) -> Unit,
    onBuySwitchChange: (CartProduct, Boolean) -> Unit,
    onAlternativeClick: (CartProductAlternative) -> Unit,
    onRemoveAlternativeClick: (CartProductAlternative) -> Unit,
    onHideAlternativesClick: (CartProduct) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp), clip = true)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        CartLookHeader(
            name = lookName,
            imageUrl = lookImageUrl,
            onAddClick = onAddClick
        )

        products.forEachIndexed { index, product ->
            when (isLargeCard) {
                true -> {
                    CartProductLargeCard(
                        product = product,
                        onClick = { onProductClick(product) },
                        onSelectSizeClick = { onSelectSizeClick(product) },
                        onBuySwitchChange = { paySwitch -> onBuySwitchChange(product, paySwitch) },
                        onAlternativeClick = onAlternativeClick,
                        onRemoveAlternativeClick = onRemoveAlternativeClick,
                        onHideAlternativesClick = { onHideAlternativesClick(product) },
                        isDividerVisible = index < products.lastIndex
                    )
                }
                false -> {
                    CartProductCard(
                        product = product,
                        onClick = { onProductClick(product) },
                        onSelectSizeClick = { onSelectSizeClick(product) },
                        onBuySwitchChange = { paySwitch -> onBuySwitchChange(product, paySwitch) },
                        onAlternativeClick = onAlternativeClick,
                        onRemoveAlternativeClick = onRemoveAlternativeClick,
                        onHideAlternativesClick = { onHideAlternativesClick(product) },
                        isDividerVisible = index < products.lastIndex
                    )
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartLookCardPreview() {
    CartLookCard(
        lookName = "Evening look",
        lookImageUrl = null,
        products = CartProductProvider().values.take(2).toList(),
        isLargeCard = false,
        onAddClick = {},
        onProductClick = {},
        onSelectSizeClick = {},
        onBuySwitchChange = { _, _ -> },
        onAlternativeClick = {},
        onRemoveAlternativeClick = {},
        onHideAlternativesClick = {}
    )
}
