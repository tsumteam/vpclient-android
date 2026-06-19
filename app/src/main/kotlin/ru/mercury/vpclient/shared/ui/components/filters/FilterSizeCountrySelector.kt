package ru.mercury.vpclient.shared.ui.components.filters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.entity.SizeCountry
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular15

@Composable
fun FilterSizeCountrySelector(
    selectedCountry: SizeCountry,
    onCountryClick: (SizeCountry) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            FilterSizeRuButton(
                selected = selectedCountry == SizeCountry.Russia,
                onClick = { onCountryClick(SizeCountry.Russia) }
            )

            FilterSizeItButton(
                selected = selectedCountry == SizeCountry.Italy,
                onClick = { onCountryClick(SizeCountry.Italy) }
            )

            FilterSizeFrButton(
                selected = selectedCountry == SizeCountry.France,
                onClick = { onCountryClick(SizeCountry.France) }
            )

            FilterSizeInternationalButton(
                selected = selectedCountry == SizeCountry.International,
                onClick = { onCountryClick(SizeCountry.International) }
            )
        }

        Text(
            text = selectedCountry.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            style = MaterialTheme.typography.regular15.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 19.sp,
                letterSpacing = .2.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun FilterSizeCountrySelectorPreview(
    @PreviewParameter(SizeCountryProvider::class) selectedCountry: SizeCountry
) {
    FilterSizeCountrySelector(
        selectedCountry = selectedCountry,
        onCountryClick = {}
    )
}

private class SizeCountryProvider: PreviewParameterProvider<SizeCountry> {
    override val values: Sequence<SizeCountry> = sequenceOf(
        SizeCountry.Russia,
        SizeCountry.Italy,
        SizeCountry.France,
        SizeCountry.International
    )
}
