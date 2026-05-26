package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings

@Composable
fun CartActionsBar(
    onFittingClick: () -> Unit,
    onBuyClick: () -> Unit,
    isActionsEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        CartActionButton(
            text = stringResource(ClientStrings.CartFitting),
            onClick = onFittingClick,
            enabled = isActionsEnabled,
            modifier = Modifier.weight(2F)
        )

        CartActionButton(
            text = stringResource(ClientStrings.CartBuy),
            onClick = onBuyClick,
            enabled = isActionsEnabled,
            modifier = Modifier.weight(1F)
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartActionsBarPreview() {
    CartActionsBar(
        onFittingClick = {},
        onBuyClick = {}
    )
}
