package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.preview.annotation.FontScalePreviews
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.surface5

@Composable
fun CartTabs(
    state: CartTabsState,
    allItemsCount: Int,
    paymentItemsCount: Int,
    onAllClick: () -> Unit,
    onPaymentClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(
                color = MaterialTheme.colorScheme.surface5,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(2.dp)
    ) {
        val tabWidth = maxWidth / 2
        val indicatorOffset = animateDpAsState(
            targetValue = when (state) {
                CartTabsState.All -> 0.dp
                CartTabsState.Payment -> tabWidth
            },
            label = "CartTabsIndicatorOffset"
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(x = indicatorOffset.value.roundToPx(), y = 0) }
                .width(tabWidth)
                .height(36.dp)
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(10.dp),
                    clip = false
                )
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.background)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CartTab(
                text = pluralStringResource(
                    ClientStrings.CartAllItems,
                    allItemsCount,
                    allItemsCount
                ),
                selected = state is CartTabsState.All,
                onClick = onAllClick,
                modifier = Modifier.weight(1F)
            )

            CartTab(
                text = pluralStringResource(
                    ClientStrings.CartPaymentItems,
                    paymentItemsCount,
                    paymentItemsCount
                ),
                selected = state is CartTabsState.Payment,
                onClick = onPaymentClick,
                modifier = Modifier.weight(1F)
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@FontScalePreviews
@Composable
private fun CartTabsPreview() {
    CartTabs(
        state = CartTabsState.All,
        allItemsCount = 100,
        paymentItemsCount = 0,
        onAllClick = {},
        onPaymentClick = {}
    )
}
