package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.data.entity.BrandFilterValue

class BrandFilterValueProvider: PreviewParameterProvider<BrandFilterValue> {
    override val values: Sequence<BrandFilterValue> = sequenceOf(
        BrandFilterValue(
            id = "mercury",
            label = "Mercury",
            labelPhotoUrl = null,
            isFavorite = true,
            isTopBrand = true
        ),
        BrandFilterValue(
            id = "loewe",
            label = "LOEWE",
            labelPhotoUrl = "https://example.com/brand-logo.png",
            isFavorite = true,
            isTopBrand = true
        )
    )
}
