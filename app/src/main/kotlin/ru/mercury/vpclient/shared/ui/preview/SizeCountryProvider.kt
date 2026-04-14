package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.entity.SizeCountry

class SizeCountryProvider: PreviewParameterProvider<SizeCountry> {
    override val values: Sequence<SizeCountry> = sequenceOf(
        SizeCountry.Russia,
        SizeCountry.Italy,
        SizeCountry.France,
        SizeCountry.International
    )
}
