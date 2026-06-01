package ru.mercury.vpclient.shared.ui.components.fitting

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.features.fitting_success.navigation.FittingSuccessDeliveryLine
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.regular14

@Composable
fun FittingSuccessDeliveryText(
    deliveryLines: List<FittingSuccessDeliveryLine>,
    address: String,
    modifier: Modifier = Modifier
) {
    val accentColor = MaterialTheme.colorScheme.error

    Text(
        text = buildAnnotatedString {
            append(stringResource(ClientStrings.FittingSuccessDeliveryPrefix))
            withStyle(SpanStyle(color = accentColor)) {
                deliveryLines.forEachIndexed { index, line ->
                    if (index > 0) append("\n")
                    append(line.intervalSummary)
                    append(" (")
                    append(
                        pluralStringResource(
                            ClientStrings.FittingConfirmationDeliveryProductsCount,
                            line.productsCount,
                            line.productsCount
                        )
                    )
                    append(")")
                }
            }
            append("\n")
            append(stringResource(ClientStrings.FittingSuccessDeliveryAddressPrefix).trimEnd())
            append(" ")
            withStyle(SpanStyle(color = accentColor)) {
                append(address)
            }
        },
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.regular14.copy(
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 19.sp,
            textAlign = TextAlign.Center,
            letterSpacing = .2.sp
        )
    )
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun FittingSuccessDeliveryTextPreview() {
    FittingSuccessDeliveryText(
        deliveryLines = listOf(
            FittingSuccessDeliveryLine(
                intervalSummary = "13 мая c 10:00 до 12:00",
                productsCount = 2
            )
        ),
        address = "Brioni, Третьяковский проезд."
    )
}
