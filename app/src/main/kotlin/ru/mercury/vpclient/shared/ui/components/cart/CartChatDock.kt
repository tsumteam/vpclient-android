package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.icons.Chat24
import ru.mercury.vpclient.shared.ui.icons.ChevronUp24
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.medium14
import ru.mercury.vpclient.shared.ui.theme.regular15
import kotlin.math.min

data class CartChatDockState(
    val name: String,
    val brand: String,
    val onClick: () -> Unit
)

@Composable
fun CartChatDock(
    state: CartChatDockState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(91.dp)
            .shadow(
                elevation = 8.dp,
                shape = CartChatDockShape,
                clip = false
            )
            .clip(CartChatDockShape)
            .background(MaterialTheme.colorScheme.background)
            .clickable(onClick = state.onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = ChevronUp24,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(.44F)
                .padding(horizontal = 4.dp)
                .height(24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Chat24,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = state.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.medium14.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                    lineHeight = 16.sp
                ),
                modifier = Modifier.weight(1F, fill = false)
            )
        }

        Text(
            text = state.brand,
            maxLines = 1,
            style = MaterialTheme.typography.regular15.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp,
                letterSpacing = .2.sp
            )
        )
    }
}

private val CartChatDockShape = GenericShape { size, _ ->
    val footTop = size.height * (47F / 91F)
    val centerWidth = size.width * .44F
    val centerStart = (size.width - centerWidth) / 2F
    val centerEnd = centerStart + centerWidth
    val radius = min(size.height * .1F, size.width * .04F)
    moveTo(0F, size.height)
    lineTo(0F, footTop)
    lineTo(centerStart, footTop)
    lineTo(centerStart, radius)
    quadraticTo(centerStart, 0F, centerStart + radius, 0F)
    lineTo(centerEnd - radius, 0F)
    quadraticTo(centerEnd, 0F, centerEnd, radius)
    lineTo(centerEnd, footTop)
    lineTo(size.width, footTop)
    lineTo(size.width, size.height)
    close()
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartChatDockPreview(
    @PreviewParameter(CartChatDockPreviewProvider::class) state: CartChatDockState
) {
    CartChatDock(
        state = state
    )
}

private class CartChatDockPreviewProvider: PreviewParameterProvider<CartChatDockState> {

    override val values: Sequence<CartChatDockState> = sequenceOf(
        CartChatDockState(
            name = "Катя",
            brand = "Brioni",
            onClick = {}
        ),
        CartChatDockState(
            name = "Екатеринищещещещеще",
            brand = "Brioni",
            onClick = {}
        ),
        CartChatDockState(
            name = "",
            brand = "",
            onClick = {}
        )
    )
}
