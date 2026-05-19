package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.components.SharedOutlinedButton
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.regular14
import ru.mercury.vpclient.shared.ui.theme.regular15
import ru.mercury.vpclient.shared.ui.theme.secondary6

@Composable
fun CartAlternativesEmpty(
    modifier: Modifier = Modifier,
    onHideClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(ClientStrings.CartAlternativesNotFound),
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.regular14.copy(
                color = MaterialTheme.colorScheme.secondary6,
                lineHeight = 18.sp,
                letterSpacing = .2.sp
            )
        )

        SharedOutlinedButton(
            onClick = onHideClick,
            text = stringResource(ClientStrings.CartHideAlternatives),
            textStyle = MaterialTheme.typography.regular15.copy(
                color = MaterialTheme.colorScheme.onBackground,
                lineHeight = 19.sp,
                letterSpacing = .2.sp
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CartAlternativesEmptyPreview() {
    CartAlternativesEmpty()
}
