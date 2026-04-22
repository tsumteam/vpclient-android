package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.data.entity.BrandEntity

class BrandEntityProvider: PreviewParameterProvider<BrandEntity> {
    override val values: Sequence<BrandEntity> = sequenceOf(
        BrandEntity(
            brand = "SAINT LAURENT",
            urlBrandLogo = null
        ),
        BrandEntity(
            brand = "GUCCI",
            urlBrandLogo = "https://example.com/brand-logo.png"
        )
    )
}
