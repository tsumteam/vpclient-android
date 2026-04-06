package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.entity.SizeCountry

class SizeCountryProvider: PreviewParameterProvider<SizeCountry> {
    override val values: Sequence<SizeCountry> = sequenceOf(
        SizeCountry.Russia,
        SizeCountry.Italy,
        SizeCountry.France,
        SizeCountry.International
    )
}
