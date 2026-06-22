package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.blue
import ru.mercury.vpclient.shared.ui.theme.regular9

data class CartProductDateReceiptBadgeState(
    val text: String,
    val isOverdue: Boolean
)

@Composable
fun CartProductDateReceiptBadge(
    state: CartProductDateReceiptBadgeState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(20.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(4.dp),
                clip = false
            )
            .background(
                color = when {
                    state.isOverdue -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.blue
                },
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = state.text,
            style = MaterialTheme.typography.regular9.copy(
                color = MaterialTheme.colorScheme.onPrimary,
                lineHeight = 12.sp
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview
@Composable
private fun CartProductDateReceiptBadgePreview(
    @PreviewParameter(CartProductDateReceiptBadgeStateProvider::class) state: CartProductDateReceiptBadgeState
) {
    CartProductDateReceiptBadge(
        state = state
    )
}

private class CartProductDateReceiptBadgeStateProvider: PreviewParameterProvider<CartProductDateReceiptBadgeState> {
    override val values: Sequence<CartProductDateReceiptBadgeState> = sequenceOf(
        CartProductDateReceiptBadgeState(
            text = "Выкупить до 23 июня",
            isOverdue = false
        ),
        CartProductDateReceiptBadgeState(
            text = "Выкупить до 23 июня",
            isOverdue = true
        )
    )
}
