package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.CartProductAlternative
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium14

data class CartAlternativesSectionState(
    val alternatives: List<CartProductAlternative>,
    val selectedAlternativeId: String? = null
) {
    val highlightedAlternativeId: String?
        get() = alternatives.firstOrNull { it.isOriginal }?.id ?: selectedAlternativeId
}

@Composable
fun CartAlternativesSection(
    state: CartAlternativesSectionState,
    onAlternativeClick: (CartProductAlternative) -> Unit,
    onRemoveClick: (CartProductAlternative) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(ClientStrings.CartSelectAlternative),
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
            style = MaterialTheme.typography.medium14.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 16.sp,
                textAlign = TextAlign.Center
            )
        )

        LazyRow {
            itemsIndexed(
                items = state.alternatives,
                key = { _, alternative -> alternative.id }
            ) { index, alternative ->
                CartAlternativeProductCard(
                    alternative = alternative,
                    isStartBorderVisible = index == 0,
                    isHighlighted = alternative.id == state.highlightedAlternativeId,
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
    @PreviewParameter(CartAlternativesSectionStateProvider::class) state: CartAlternativesSectionState
) {
    CartAlternativesSection(
        state = state,
        onAlternativeClick = {},
        onRemoveClick = {}
    )
}

private class CartAlternativesSectionStateProvider: PreviewParameterProvider<CartAlternativesSectionState> {

    private val alternatives = listOf(
        CartProductAlternative(
            id = "1",
            detailId = "1",
            brand = "BALMAIN",
            urlBrandLogo = null,
            price = "580 000 ₽",
            imageUrl = "",
            isOriginal = true
        ),
        CartProductAlternative(
            id = "2",
            detailId = "2",
            brand = "DOLCE&GABBANA",
            urlBrandLogo = null,
            price = "1 900 000 ₽",
            imageUrl = "",
            isOriginal = false
        ),
        CartProductAlternative(
            id = "3",
            detailId = "3",
            brand = "MVST",
            urlBrandLogo = null,
            price = "800 000 ₽",
            imageUrl = "",
            isOriginal = false
        )
    )

    override val values: Sequence<CartAlternativesSectionState> = sequenceOf(
        CartAlternativesSectionState(
            alternatives = alternatives
        ),
        CartAlternativesSectionState(
            alternatives = alternatives.map { alternative -> alternative.copy(isOriginal = false) },
            selectedAlternativeId = "2"
        )
    )
}
