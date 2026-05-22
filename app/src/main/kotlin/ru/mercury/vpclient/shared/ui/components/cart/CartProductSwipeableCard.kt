package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlinx.coroutines.launch
import ru.mercury.vpclient.shared.ui.icons.Edit24
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.cartSwipeEdit

private const val SWIPE_ACTION_RETURN_DURATION = 180

@Composable
fun CartProductSwipeableCard(
    leadingActionsContent: @Composable (Float, (() -> Unit) -> Unit) -> Unit = { _, _ -> },
    trailingActionsContent: @Composable (Float, (() -> Unit) -> Unit) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier,
    leadingSwipeSize: Dp = 0.dp,
    trailingSwipeSize: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    val leadingSizePx = with(LocalDensity.current) { leadingSwipeSize.toPx() }
    val trailingSizePx = with(LocalDensity.current) { trailingSwipeSize.toPx() }
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0F) }
    val onSwipeActionClick: (() -> Unit) -> Unit = { action ->
        scope.launch {
            offsetX.animateTo(
                targetValue = 0F,
                animationSpec = tween(durationMillis = SWIPE_ACTION_RETURN_DURATION)
            )
            action()
        }
    }
    val leadingSwipeProgress = when {
        leadingSizePx == 0F -> 0F
        offsetX.value > 0F -> (offsetX.value / leadingSizePx).coerceIn(0F, 1F)
        else -> 0F
    }
    val trailingSwipeProgress = when {
        trailingSizePx == 0F -> 0F
        offsetX.value < 0F -> (abs(offsetX.value) / trailingSizePx).coerceIn(0F, 1F)
        else -> 0F
    }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.matchParentSize(),
            horizontalArrangement = Arrangement.Start
        ) {
            leadingActionsContent(leadingSwipeProgress, onSwipeActionClick)
        }

        Row(
            modifier = Modifier.matchParentSize(),
            horizontalArrangement = Arrangement.End
        ) {
            trailingActionsContent(trailingSwipeProgress, onSwipeActionClick)
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.toInt(), 0) }
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .pointerInput(leadingSizePx, trailingSizePx) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            val newOffsetX = (offsetX.value + dragAmount).coerceIn(-trailingSizePx, leadingSizePx)
                            scope.launch { offsetX.snapTo(newOffsetX) }
                        },
                        onDragEnd = {
                            val endOffsetX = when {
                                offsetX.value > leadingSizePx * .3F -> leadingSizePx
                                offsetX.value < -trailingSizePx * .3F -> -trailingSizePx
                                else -> 0F
                            }
                            scope.launch { offsetX.animateTo(endOffsetX) }
                        }
                    )
                }
        ) {
            content()
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun CartProductSwipeableCardPreview() {
    CartProductSwipeableCard(
        trailingActionsContent = { swipeProgress, onSwipeActionClick ->
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(88.dp * swipeProgress)
                    .clipToBounds(),
                contentAlignment = Alignment.CenterStart
            ) {
                CartProductSwipeAction(
                    imageVector = Edit24,
                    text = "Изменить",
                    backgroundColor = MaterialTheme.colorScheme.cartSwipeEdit,
                    onClick = { onSwipeActionClick {} }
                )
            }
        },
        trailingSwipeSize = 88.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(178.dp)
                .background(MaterialTheme.colorScheme.surface)
        )
    }
}
