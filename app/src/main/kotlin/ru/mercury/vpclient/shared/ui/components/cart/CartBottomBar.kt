package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
fun CartBottomBar(
    onFittingClick: () -> Unit,
    onBuyClick: () -> Unit,
    onChatClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CartActionButton(
                text = stringResource(ClientStrings.CartFitting),
                onClick = onFittingClick,
                modifier = Modifier.weight(2F)
            )

            CartActionButton(
                text = stringResource(ClientStrings.CartBuy),
                onClick = onBuyClick,
                modifier = Modifier.weight(1F)
            )
        }

        CartChatDock(
            onClick = onChatClick
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartBottomBarPreview() {
    CartBottomBar(
        onFittingClick = {},
        onBuyClick = {},
        onChatClick = {}
    )
}
