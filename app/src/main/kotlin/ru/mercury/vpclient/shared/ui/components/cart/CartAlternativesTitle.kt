package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium14

@Composable
fun CartAlternativesTitle(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(ClientStrings.CartSelectAlternative),
        modifier = modifier
            .fillMaxWidth()
            .height(20.dp),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.medium14.copy(
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 16.sp
        )
    )
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartAlternativesTitlePreview() {
    CartAlternativesTitle()
}
