package ru.mercury.vpclient.shared.ui.components.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.data.entity.BrandFilterValue
import ru.mercury.vpclient.shared.ui.preview.BrandFilterValuesProvider
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

@Composable
fun BrandChipsGrid(
    brands: List<BrandFilterValue>,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        brands.chunked(3).forEach { rowBrands ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowBrands.forEach { brand ->
                    BrandChip(
                        brand = brand,
                        onClick = { onToggle(brand.id) },
                        modifier = Modifier.weight(1F)
                    )
                }
                repeat(3 - rowBrands.size) {
                    Spacer(
                        modifier = Modifier.weight(1F)
                    )
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun BrandChipsGridPreview(
    @PreviewParameter(BrandFilterValuesProvider::class) brands: List<BrandFilterValue>
) {
    BrandChipsGrid(
        brands = brands,
        onToggle = {},
        modifier = Modifier.padding(vertical = 16.dp)
    )
}
