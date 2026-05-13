package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.ui.icons.CardDiscount
import ru.mercury.vpclient.shared.ui.preview.CartProductProvider
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

@Composable
fun CartPriceRow(
    product: CartProduct,
    modifier: Modifier = Modifier
) {
    val isPriceVisible = product.priceValue > .0 && product.price.isNotBlank()
    val isDiscountVisible = product.hasActions && product.discountPercentage >= 0 && isPriceVisible

    when {
        isPriceVisible -> {
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isDiscountVisible) {
                    Icon(
                        imageVector = CardDiscount,
                        contentDescription = null,
                        modifier = Modifier.size(width = 32.dp, height = 20.dp),
                        tint = Color.Unspecified
                    )
                }

                CartPriceText(
                    product = product
                )
            }
        }
        else -> {
            Row(
                modifier = modifier
            ) {}
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartPriceRowPreview(
    @PreviewParameter(CartProductProvider::class) product: CartProduct
) {
    CartPriceRow(
        product = product
    )
}
