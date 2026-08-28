package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.BrandEntity
import ru.mercury.vpclient.shared.ui.components.product.ProductBrandBox
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.regular14

data class CartProductHeaderState(
    val brandEntity: BrandEntity,
    val isSold: Boolean = false,
    val isForPayment: Boolean = false,
    val onBuySwitchChange: (Boolean) -> Unit = {}
) {
    val isSoldLabelVisible: Boolean
        get() = isSold

    val isBuySwitchVisible: Boolean
        get() = !isSold
}

@Composable
fun CartProductHeader(
    state: CartProductHeaderState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProductBrandBox(
            entity = state.brandEntity,
            modifier = Modifier
                .padding(top = 4.dp)
                .height(24.dp)
                .weight(1F)
        )

        when {
            state.isSoldLabelVisible -> {
                Text(
                    text = stringResource(ClientStrings.CartSold),
                    modifier = Modifier.padding(top = 9.dp),
                    style = MaterialTheme.typography.regular14.copy(
                        color = MaterialTheme.colorScheme.error,
                        lineHeight = 18.sp,
                        letterSpacing = .2.sp
                    )
                )
            }
            state.isBuySwitchVisible -> {
                CartBuySwitch(
                    checked = state.isForPayment,
                    onCheckedChange = state.onBuySwitchChange,
                    modifier = Modifier.height(32.dp)
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartProductHeaderPreview(
    @PreviewParameter(CartProductHeaderStatePreviewParameterProvider::class) state: CartProductHeaderState
) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        CartProductHeader(
            state = state
        )
    }
}

private class CartProductHeaderStatePreviewParameterProvider: PreviewParameterProvider<CartProductHeaderState> {
    override val values: Sequence<CartProductHeaderState> = sequenceOf(
        CartProductHeaderState(
            brandEntity = BrandEntity(
                brand = "SAINT LAURENT",
                urlBrandLogo = null
            ),
            isForPayment = true
        ),
        CartProductHeaderState(
            brandEntity = BrandEntity(
                brand = "BRUNELLO CUCINELLI",
                urlBrandLogo = null
            ),
            isForPayment = false
        ),
        CartProductHeaderState(
            brandEntity = BrandEntity(
                brand = "LORO PIANA",
                urlBrandLogo = null
            ),
            isSold = true
        ),
        CartProductHeaderState(
            brandEntity = BrandEntity(
                brand = "GUCCI",
                urlBrandLogo = "https://example.com/brand-logo.png"
            ),
            isForPayment = true
        )
    )
}
