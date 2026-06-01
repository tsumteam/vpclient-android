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
import ru.mercury.vpclient.shared.ui.icons.BasketFilled24
import ru.mercury.vpclient.shared.ui.icons.Delete24
import ru.mercury.vpclient.shared.ui.icons.Edit24
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.surface2

@Composable
fun CartProductTrailingSwipeActions(
    swipeProgress: Float,
    isEditVisible: Boolean,
    isDetachFromLookVisible: Boolean,
    isDeleteVisible: Boolean,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit = {},
    onDetachFromLookClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    val actionWidth = 88.dp * swipeProgress

    if (isEditVisible) {
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
    }

    if (isDetachFromLookVisible) {
        Box(
            modifier = modifier
                .fillMaxHeight()
                .width(actionWidth)
                .clipToBounds(),
            contentAlignment = Alignment.CenterStart
        ) {
            CartProductSwipeAction(
                imageVector = BasketFilled24,
                text = stringResource(ClientStrings.CartDetachFromLook),
                backgroundColor = MaterialTheme.colorScheme.secondary,
                onClick = onDetachFromLookClick
            )
        }
    }

    if (isDeleteVisible) {
        Box(
            modifier = modifier
                .fillMaxHeight()
                .width(actionWidth)
                .clipToBounds(),
            contentAlignment = Alignment.CenterStart
        ) {
            CartProductSwipeAction(
                imageVector = Delete24,
                text = stringResource(ClientStrings.CartDelete),
                backgroundColor = MaterialTheme.colorScheme.error,
                onClick = onDeleteClick
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartProductTrailingSwipeActionsPreview() {
    Row(
        modifier = Modifier.height(178.dp)
    ) {
        CartProductTrailingSwipeActions(
            swipeProgress = 1F,
            isEditVisible = true,
            isDetachFromLookVisible = true,
            isDeleteVisible = true
        )
    }
}
