package ru.mercury.vpclient.shared.ui.components.cart

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import ru.mercury.vpclient.shared.ui.icons.Edit24
import ru.mercury.vpclient.shared.ui.preview.wrapper.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.cartSwipeEdit

@Composable
fun CartProductSwipeableCard(
    swipeActionsContent: @Composable (Float) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val swipeSize = 176.dp
    val sizePx = with(LocalDensity.current) { swipeSize.toPx() }
    var offsetX by remember { mutableFloatStateOf(0F) }
    val animatedOffsetX by animateFloatAsState(targetValue = offsetX, label = "cartProductSwipeOffset")
    val swipeProgress = (abs(animatedOffsetX) / sizePx).coerceIn(0F, 1F)

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.matchParentSize(),
            horizontalArrangement = Arrangement.End
        ) {
            swipeActionsContent(swipeProgress)
        }

        Box(
            modifier = Modifier
                .offset { IntOffset(animatedOffsetX.toInt(), 0) }
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .pointerInput(sizePx) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            val newOffsetX = (offsetX + dragAmount).coerceIn(-sizePx, 0F)
                            offsetX = newOffsetX
                        },
                        onDragEnd = {
                            val endOffsetX = when {
                                abs(offsetX) > sizePx * .3F -> -sizePx
                                else -> 0F
                            }
                            offsetX = endOffsetX
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
        swipeActionsContent = { swipeProgress ->
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
                    backgroundColor = MaterialTheme.colorScheme.cartSwipeEdit
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(178.dp)
                .background(MaterialTheme.colorScheme.surface)
        )
    }
}
