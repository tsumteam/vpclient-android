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
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.cartSwipeAlternatives
import ru.mercury.vpclient.shared.ui.theme.cartSwipeReturnOriginal

@Composable
fun CartProductLeadingSwipeActions(
    swipeProgress: Float,
    isReturnOriginalVisible: Boolean,
    isShowAlternativesVisible: Boolean,
    isHideAlternativesVisible: Boolean,
    modifier: Modifier = Modifier
) {
    val actionWidth = 88.dp * swipeProgress

    if (isReturnOriginalVisible) {
        Box(
            modifier = modifier
                .fillMaxHeight()
                .width(actionWidth)
                .clipToBounds(),
            contentAlignment = Alignment.CenterEnd
        ) {
            CartProductSwipeAction(
                imageVector = null,
                text = stringResource(ClientStrings.CartReturnOriginal),
                backgroundColor = MaterialTheme.colorScheme.cartSwipeReturnOriginal,
                contentHorizontalAlignment = Alignment.End
            )
        }
    }

    if (isShowAlternativesVisible) {
        Box(
            modifier = modifier
                .fillMaxHeight()
                .width(actionWidth)
                .clipToBounds(),
            contentAlignment = Alignment.CenterEnd
        ) {
            CartProductSwipeAction(
                imageVector = null,
                text = stringResource(ClientStrings.CartShowAlternatives),
                backgroundColor = MaterialTheme.colorScheme.cartSwipeAlternatives,
                contentColor = MaterialTheme.colorScheme.onBackground,
                contentHorizontalAlignment = Alignment.End
            )
        }
    }

    if (isHideAlternativesVisible) {
        Box(
            modifier = modifier
                .fillMaxHeight()
                .width(actionWidth)
                .clipToBounds(),
            contentAlignment = Alignment.CenterEnd
        ) {
            CartProductSwipeAction(
                imageVector = null,
                text = stringResource(ClientStrings.CartHideAlternativesList),
                backgroundColor = MaterialTheme.colorScheme.cartSwipeAlternatives,
                contentColor = MaterialTheme.colorScheme.onBackground,
                contentHorizontalAlignment = Alignment.End
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartProductLeadingSwipeActionsPreview() {
    Row(
        modifier = Modifier.height(178.dp)
    ) {
        CartProductLeadingSwipeActions(
            swipeProgress = 1F,
            isReturnOriginalVisible = true,
            isShowAlternativesVisible = true,
            isHideAlternativesVisible = true
        )
    }
}
