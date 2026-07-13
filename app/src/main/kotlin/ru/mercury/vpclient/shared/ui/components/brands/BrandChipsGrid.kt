package ru.mercury.vpclient.shared.ui.components.brands

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.BrandFilterValue
import ru.mercury.vpclient.shared.ui.components.brands.BrandChipsGridState.Companion.COLUMN_COUNT
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper

data class BrandChipsGridState(
    val brands: List<BrandFilterValue>,
    val onToggle: (String) -> Unit
) {
    companion object {
        const val COLUMN_COUNT = 3
    }
}

@Composable
fun BrandChipsGrid(
    state: BrandChipsGridState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        state.brands.chunked(COLUMN_COUNT).forEach { rowBrands ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowBrands.forEach { brand ->
                    BrandChip(
                        state = BrandChipState(
                            brand = brand,
                            onClick = { state.onToggle(brand.id) }
                        ),
                        modifier = Modifier.weight(1F)
                    )
                }
                repeat(COLUMN_COUNT - rowBrands.size) {
                    Spacer(
                        modifier = Modifier.weight(1F)
                    )
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun BrandChipsGridPreview(
    @PreviewParameter(BrandChipsGridStateProvider::class) state: BrandChipsGridState
) {
    BrandChipsGrid(
        state = state,
        modifier = Modifier.padding(vertical = 16.dp)
    )
}

private class BrandChipsGridStateProvider: PreviewParameterProvider<BrandChipsGridState> {
    override val values: Sequence<BrandChipsGridState> = sequenceOf(
        BrandChipsGridState(
            brands = listOf(
                BrandFilterValue(
                    id = "mercury",
                    label = "Mercury",
                    labelPhotoUrl = null,
                    isFavorite = true,
                    isTopBrand = true
                ),
                BrandFilterValue(
                    id = "dolce-gabbana",
                    label = "Dolce & Gabbana",
                    labelPhotoUrl = null,
                    isFavorite = true,
                    isTopBrand = false
                ),
                BrandFilterValue(
                    id = "loewe",
                    label = "LOEWE",
                    labelPhotoUrl = "https://example.com/brand-logo.png",
                    isFavorite = false,
                    isTopBrand = true
                ),
                BrandFilterValue(
                    id = "prada",
                    label = "Prada",
                    labelPhotoUrl = null,
                    isFavorite = false,
                    isTopBrand = true
                )
            ),
            onToggle = {}
        )
    )
}
