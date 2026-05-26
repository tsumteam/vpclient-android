package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper

@Composable
fun CartBottomBar(
    chatName: String,
    chatBrand: String,
    onFittingClick: () -> Unit,
    onBuyClick: () -> Unit,
    onChatClick: () -> Unit,
    isActionsEnabled: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
    ) {
        CartActionsBar(
            onFittingClick = onFittingClick,
            onBuyClick = onBuyClick,
            isActionsEnabled = isActionsEnabled,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        CartChatDock(
            name = chatName,
            brand = chatBrand,
            onClick = onChatClick
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartBottomBarPreview() {
    CartBottomBar(
        chatName = "Екатерина",
        chatBrand = "Brioni",
        onFittingClick = {},
        onBuyClick = {},
        onChatClick = {}
    )
}
