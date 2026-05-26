package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.ktx.clickableWithoutRipple
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.medium13

@Composable
fun CartFittingSwitch(
    selectedIndex: Int,
    onCartClick: () -> Unit,
    onFittingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val shape = RoundedCornerShape(48.dp)
    var cartTabWidth by remember { mutableStateOf(0.dp) }
    var fittingTabWidth by remember { mutableStateOf(0.dp) }
    val indicatorOffset by animateDpAsState(
        targetValue = when (selectedIndex) {
            0 -> 0.dp
            else -> cartTabWidth
        },
        label = "CartFittingSwitchIndicatorOffset"
    )
    val indicatorWidth by animateDpAsState(
        targetValue = when (selectedIndex) {
            0 -> cartTabWidth
            else -> fittingTabWidth
        },
        label = "CartFittingSwitchIndicatorWidth"
    )

    Box(
        modifier = modifier
            .height(40.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = shape
            )
            .padding(4.dp)
    ) {
        if (indicatorWidth > 0.dp) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(x = indicatorOffset.roundToPx(), y = 0) }
                    .width(indicatorWidth)
                    .height(32.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = shape,
                        clip = false
                    )
                    .clip(shape)
                    .background(MaterialTheme.colorScheme.background)
            )
        }

        Row(
            modifier = Modifier.height(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CartFittingSwitchItem(
                text = stringResource(ClientStrings.CartToolbarCart),
                selected = selectedIndex == 0,
                onClick = onCartClick,
                onWidthChange = { width ->
                    cartTabWidth = with(density) { width.toDp() }
                }
            )

            CartFittingSwitchItem(
                text = stringResource(ClientStrings.CartToolbarFitting),
                selected = selectedIndex == 1,
                onClick = onFittingClick,
                onWidthChange = { width ->
                    fittingTabWidth = with(density) { width.toDp() }
                }
            )
        }
    }
}

@Composable
private fun CartFittingSwitchItem(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    onWidthChange: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .height(32.dp)
            .clip(RoundedCornerShape(48.dp))
            .clickableWithoutRipple(onClick = onClick)
            .onSizeChanged { size -> onWidthChange(size.width) }
            .padding(start = 12.dp, end = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.medium13.copy(
                color = when (selected) {
                    true -> MaterialTheme.colorScheme.onBackground
                    false -> MaterialTheme.colorScheme.outline
                },
                lineHeight = 16.sp,
                textAlign = TextAlign.Center
            )
        )
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartFittingSwitchCartPreview() {
    CartFittingSwitch(
        selectedIndex = 0,
        onCartClick = {},
        onFittingClick = {}
    )
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartFittingSwitchFittingPreview() {
    CartFittingSwitch(
        selectedIndex = 1,
        onCartClick = {},
        onFittingClick = {}
    )
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartFittingSwitchItemPreview() {
    CartFittingSwitchItem(
        text = "Корзина",
        selected = true,
        onClick = {},
        onWidthChange = {}
    )
}
