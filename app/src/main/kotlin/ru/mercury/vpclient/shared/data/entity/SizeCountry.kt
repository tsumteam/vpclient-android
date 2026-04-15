package ru.mercury.vpclient.shared.data.entity

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.mercury.vpclient.shared.ui.theme.ClientStrings

sealed class SizeCountry {
    data object Russia: SizeCountry()
    data object Italy: SizeCountry()
    data object France: SizeCountry()
    data object International: SizeCountry()

    val title: String
        @Composable get() = when (this) {
            Russia -> stringResource(ClientStrings.SizeCountryRussia)
            Italy -> stringResource(ClientStrings.SizeCountryItaly)
            France -> stringResource(ClientStrings.SizeCountryFrance)
            International -> stringResource(ClientStrings.SizeCountryInternational)
        }

    // fixme
    fun label(value: SizeFilterValue): String? {
        return when (this) {
            Russia -> value.labelRu
            Italy -> value.labelItalian
            France -> value.labelFrench
            International -> value.labelInternational
        }
    }
}
