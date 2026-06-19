package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium15

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 4.dp, end = 16.dp, bottom = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onFittingClick,
                enabled = isActionsEnabled,
                modifier = Modifier
                    .weight(2F)
                    .height(46.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = stringResource(ClientStrings.CartFitting),
                    style = MaterialTheme.typography.medium15.copy(
                        letterSpacing = .3.sp
                    )
                )
            }

            Button(
                onClick = onBuyClick,
                enabled = isActionsEnabled,
                modifier = Modifier
                    .weight(1F)
                    .height(46.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = stringResource(ClientStrings.CartBuy),
                    style = MaterialTheme.typography.medium15.copy(
                        letterSpacing = .3.sp
                    )
                )
            }
        }

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
