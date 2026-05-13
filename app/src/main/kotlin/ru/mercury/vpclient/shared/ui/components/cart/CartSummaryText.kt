package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun CartSummaryText(
    label: String,
    value: String
) {
    Row {
        Text(
            text = label,
            style = MaterialTheme.typography.medium14.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 16.sp,
                textAlign = TextAlign.Center
            )
        )

        Text(
            text = value,
            style = MaterialTheme.typography.regular14.copy(
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = .2.sp,
                lineHeight = 16.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartSummaryTextPreview() {
    CartSummaryText(
        label = "В корзине: ",
        value = "5 товаров на сумму 4 600 000 ₽"
    )
}
