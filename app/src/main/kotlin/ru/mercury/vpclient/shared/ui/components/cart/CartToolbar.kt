package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.features.cart.model.CartViewMode
import ru.mercury.vpclient.shared.ui.icons.Card24
import ru.mercury.vpclient.shared.ui.icons.List24
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.secondary4

@Composable
fun CartToolbar(
    tabsState: CartTabsState,
    viewMode: CartViewMode,
    isViewModeSwitcherVisible: Boolean,
    allItemsCount: Int,
    paymentItemsCount: Int,
    onAllClick: () -> Unit,
    onPaymentClick: () -> Unit,
    onCardsClick: () -> Unit,
    onListClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(start = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val cardIconTint = animateColorAsState(
            targetValue = when (viewMode) {
                CartViewMode.List -> MaterialTheme.colorScheme.secondary4
                CartViewMode.Cards -> MaterialTheme.colorScheme.onBackground
            },
            label = "CartToolbarCardIconTint"
        )
        val listIconTint = animateColorAsState(
            targetValue = when (viewMode) {
                CartViewMode.List -> MaterialTheme.colorScheme.onBackground
                CartViewMode.Cards -> MaterialTheme.colorScheme.secondary4
            },
            label = "CartToolbarListIconTint"
        )

        CartTabs(
            state = tabsState,
            allItemsCount = allItemsCount,
            paymentItemsCount = paymentItemsCount,
            onAllClick = onAllClick,
            onPaymentClick = onPaymentClick
        )

        Spacer(
            modifier = Modifier.weight(1F)
        )

        if (isViewModeSwitcherVisible) {
            Row {
                IconButton(
                    onClick = onCardsClick
                ) {
                    Icon(
                        imageVector = Card24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = cardIconTint.value
                    )
                }

                IconButton(
                    onClick = onListClick
                ) {
                    Icon(
                        imageVector = List24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = listIconTint.value
                    )
                }
            }
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartToolbarPreview() {
    CartToolbar(
        tabsState = CartTabsState.All,
        viewMode = CartViewMode.List,
        isViewModeSwitcherVisible = true,
        allItemsCount = 100,
        paymentItemsCount = 2,
        onAllClick = {},
        onPaymentClick = {},
        onCardsClick = {},
        onListClick = {}
    )
}
