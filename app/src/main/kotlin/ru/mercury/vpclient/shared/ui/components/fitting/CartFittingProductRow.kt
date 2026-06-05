package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.ui.components.cart.CartAsyncImage
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular14

data class FittingProductRowState(
    val product: CartProduct,
    val checked: Boolean
)

@Composable
fun FittingProductRow(
    state: FittingProductRowState,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(128.dp)
            .clickable { onCheckedChange(!state.checked) }
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = state.checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.outline,
                checkmarkColor = MaterialTheme.colorScheme.onPrimary
            )
        )

        CartAsyncImage(
            imageUrl = state.product.imageUrl,
            modifier = Modifier.size(width = 64.dp, height = 96.dp)
        )

        Column(
            modifier = Modifier.weight(1F),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = state.product.brand,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = .2.sp
                )
            )

            Text(
                text = state.product.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )
        }

        Column(
            modifier = Modifier.width(112.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = state.product.price,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )

            Text(
                text = state.product.size,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 18.sp,
                    letterSpacing = .2.sp
                )
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun FittingProductRowPreview(
    @PreviewParameter(FittingProductRowStateProvider::class) state: FittingProductRowState
) {
    FittingProductRow(
        state = state,
        onCheckedChange = {}
    )
}

private class FittingProductRowStateProvider: PreviewParameterProvider<FittingProductRowState> {
    override val values: Sequence<FittingProductRowState> = sequenceOf(
        FittingProductRowState(
            product = CartProduct(
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
            checked = true
        ),
        FittingProductRowState(
            product = CartProduct(
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
                imageUrl = "",
                isForPayment = false,
                priceValue = 300_000.0
            ),
            checked = false
        )
    )
}
