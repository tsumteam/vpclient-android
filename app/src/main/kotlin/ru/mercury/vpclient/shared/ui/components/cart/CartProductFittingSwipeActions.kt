package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.icons.Edit24
import ru.mercury.vpclient.shared.ui.icons.ReturnToBasket24
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.surface2

@Composable
fun CartProductFittingSwipeActions(
    swipeProgress: Float,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit = {},
    onReturnToBasketClick: () -> Unit = {}
) {
    val actionWidth = 88.dp * swipeProgress

    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(actionWidth)
            .clipToBounds(),
        contentAlignment = Alignment.CenterStart
    ) {
        CartProductSwipeAction(
            imageVector = Edit24,
            text = stringResource(ClientStrings.CartEdit),
            backgroundColor = MaterialTheme.colorScheme.surface2,
            onClick = onEditClick
        )
    }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(actionWidth)
            .clipToBounds(),
        contentAlignment = Alignment.CenterStart
    ) {
        CartProductSwipeAction(
            imageVector = ReturnToBasket24,
            text = stringResource(ClientStrings.CartFittingReturnToBasket),
            backgroundColor = MaterialTheme.colorScheme.primary,
            onClick = onReturnToBasketClick
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartProductFittingSwipeActionsPreview() {
    Row(
        modifier = Modifier.height(178.dp)
    ) {
        CartProductFittingSwipeActions(
            swipeProgress = 1F
        )
    }
}
