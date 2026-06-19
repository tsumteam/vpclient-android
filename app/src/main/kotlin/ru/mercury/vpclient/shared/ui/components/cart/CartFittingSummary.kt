package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings

@Composable
fun CartFittingSummary(
    fittingSummary: String,
    paymentSummary: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 40.dp, bottom = 55.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CartSummaryText(
            label = stringResource(ClientStrings.CartToFitting),
            value = fittingSummary
        )

        CartSummaryText(
            label = stringResource(ClientStrings.CartToPayment),
            value = paymentSummary
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartFittingSummaryPreview() {
    CartFittingSummary(
        fittingSummary = "5 товаров на сумму 4 600 000 ₽",
        paymentSummary = "2 товара на сумму 1 200 000 ₽"
    )
}
