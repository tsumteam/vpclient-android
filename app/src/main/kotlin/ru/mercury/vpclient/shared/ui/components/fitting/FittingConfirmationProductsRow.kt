package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.CartProduct

@Composable
fun FittingConfirmationProductsRow(
    products: List<CartProduct>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(start = 16.dp, end = 16.dp)
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        items(
            items = products,
            key = { product -> product.id }
        ) { product ->
            FittingConfirmationProductCard(
                product = product
            )
        }
    }
}
