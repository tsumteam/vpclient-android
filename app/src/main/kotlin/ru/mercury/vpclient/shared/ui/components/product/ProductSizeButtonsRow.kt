package ru.mercury.vpclient.shared.ui.components.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.components.details.DetailsSizeButton
import ru.mercury.vpclient.shared.ui.components.details.SizeSelectorState
import ru.mercury.vpclient.shared.ui.components.details.SizeState
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun ProductSizeButtonsRow(
    state: SizeSelectorState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(start = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.height(54.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = state.topText,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = .2.sp
                )
            )

            Text(
                text = state.bottomText,
                style = MaterialTheme.typography.regular14.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    letterSpacing = .2.sp
                )
            )
        }

        LazyRow(
            modifier = Modifier.weight(1F),
            contentPadding = PaddingValues(end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            itemsIndexed(
                items = state.sizes
            ) { index, sizeState ->
                DetailsSizeButton(
                    state = sizeState,
                    onClick = { state.onSizeClick(index) }
                )
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun ProductSizeButtonsRowPreview(
    @PreviewParameter(ProductSizeButtonsRowStateProvider::class) state: SizeSelectorState
) {
    ProductSizeButtonsRow(
        state = state
    )
}

private class ProductSizeButtonsRowStateProvider: PreviewParameterProvider<SizeSelectorState> {

    private val sizes = listOf(
        SizeState(topText = "RU 36", bottomText = "IT 34", selected = false, enabled = true),
        SizeState(topText = "RU 38", bottomText = "IT 36", selected = true, enabled = true),
        SizeState(topText = "RU 40", bottomText = "IT 38", selected = false, enabled = false),
        SizeState(topText = "RU 42", bottomText = "IT 40", selected = false, enabled = true)
    )

    override val values: Sequence<SizeSelectorState> = sequenceOf(
        SizeSelectorState(
            sizes = sizes,
            topText = "IT",
            bottomText = "RU",
            isSizeTableVisible = true
        )
    )
}
