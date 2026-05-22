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
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.ui.preview.CartProductAlternativeProvider
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

@Composable
fun CartAlternativesSection(
    alternatives: List<CartProductAlternative>,
    modifier: Modifier = Modifier,
    selectedAlternativeId: String? = null,
    onAlternativeClick: (CartProductAlternative) -> Unit = {},
    onRemoveClick: (CartProductAlternative) -> Unit = {}
) {
    val highlightedAlternativeId = alternatives.firstOrNull { it.isOriginal }?.id ?: selectedAlternativeId

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        CartAlternativesTitle()

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        LazyRow {
            itemsIndexed(
                items = alternatives
            ) { index, alternative ->
                CartAlternativeProductCard(
                    alternative = alternative,
                    isStartBorderVisible = index == 0,
                    isHighlighted = alternative.id == highlightedAlternativeId,
                    onClick = { onAlternativeClick(alternative) },
                    onRemoveClick = { onRemoveClick(alternative) }
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartAlternativesSectionPreview(
    @PreviewParameter(CartProductAlternativeProvider::class) alternative: CartProductAlternative
) {
    CartAlternativesSection(
        alternatives = listOf(
            alternative,
            alternative.copy(id = "2", brand = "DOLCE&GABBANA", price = "1 900 000 ₽", isOriginal = false),
            alternative.copy(id = "3", brand = "MVST", price = "800 000 ₽", isOriginal = false)
        )
    )
}
