package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.data.entity.BrandFilterValue

class BrandFilterValuesProvider: PreviewParameterProvider<List<BrandFilterValue>> {
    override val values: Sequence<List<BrandFilterValue>> = sequenceOf(
        listOf(
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
        )
    )
}
