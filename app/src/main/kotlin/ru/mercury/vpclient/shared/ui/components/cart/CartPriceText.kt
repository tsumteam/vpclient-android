package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.CartProduct
import ru.mercury.vpclient.shared.ui.preview.CartProductProvider
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium14

@Composable
fun CartPriceText(
    product: CartProduct,
    modifier: Modifier = Modifier
) {
    Text(
        text = buildAnnotatedString {
            product.oldPrice?.let { oldPrice ->
                withStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        textDecoration = TextDecoration.LineThrough
                    )
                ) {
                    append(oldPrice)
                }
                append("  ")
            }
            withStyle(
                SpanStyle(
                    color = when {
                        product.oldPrice != null -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onBackground
                    }
                )
            ) {
                append(product.price)
            }
        },
        modifier = modifier,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        style = MaterialTheme.typography.medium14.copy(
            letterSpacing = .2.sp,
            lineHeight = 16.sp
        )
    )
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartPriceTextPreview(
    @PreviewParameter(CartProductProvider::class) product: CartProduct
) {
    CartPriceText(
        product = product
    )
}
