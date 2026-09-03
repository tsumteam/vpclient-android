package ru.mercury.vpclient.shared.ui.ktx

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.clickableWithoutRipple(
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        enabled = enabled,
        onClick = onClick
    )
}

@Composable
fun Modifier.blockClickable(): Modifier {
    return this.then(
        Modifier.clickable(
            enabled = true,
            indication = null,
            interactionSource = remember { MutableInteractionSource() },
            onClick = {}
        )
    )
}

/**
 * Расширяет зону интерактивности (клик и подсветку `ripple`) на [horizontal] по горизонтали и на
 * [vertical] по вертикали в каждую сторону, не меняя измеряемый размер узла.
 *
 * Вложенный контент меряется с ограничениями, увеличенными на `2 * inset` по каждой оси, поэтому
 * узел `clickable`, поставленный сразу после этого модификатора, «раздувается» на inset. Наружу
 * репортится исходный размер, а раздутый placeable размещается со сдвигом `-inset` по x и y, из-за
 * чего зона нажатия выходит за границы контента, не раздвигая соседние элементы.
 *
 * Ставь до `Modifier.clickable`. Чтобы видимый контент остался на месте, компенсируй сдвиг внутренним
 * `Modifier.padding(horizontal = horizontal, vertical = vertical)` после `clickable`.
 */
fun Modifier.clickAreaInset(
    horizontal: Dp = 0.dp,
    vertical: Dp = 0.dp
): Modifier {
    return layout { measurable, constraints ->
        val horizontalInset = horizontal.roundToPx()
        val verticalInset = vertical.roundToPx()

        val placeable = measurable.measure(
            constraints.copy(
                minWidth = (constraints.minWidth + horizontalInset * 2).coerceAtLeast(0),
                minHeight = (constraints.minHeight + verticalInset * 2).coerceAtLeast(0),
                maxWidth = if (constraints.hasBoundedWidth) constraints.maxWidth + horizontalInset * 2 else constraints.maxWidth,
                maxHeight = if (constraints.hasBoundedHeight) constraints.maxHeight + verticalInset * 2 else constraints.maxHeight
            )
        )

        val width = (placeable.width - horizontalInset * 2).coerceAtLeast(0)
        val height = (placeable.height - verticalInset * 2).coerceAtLeast(0)

        layout(width, height) {
            placeable.place(x = -horizontalInset, y = -verticalInset)
        }
    }
}

fun Modifier.disableSplitMotionEvents(): Modifier {
    return pointerInput(Unit) {
        awaitPointerEventScope {
            var activeId: PointerId? = null
            while (true) {
                val event = awaitPointerEvent(PointerEventPass.Initial)
                event.changes.forEach { change ->
                    when {
                        change.pressed && activeId == null -> activeId = change.id
                        !change.pressed && change.id == activeId -> activeId = null
                        change.id != activeId && activeId != null -> change.consume()
                    }
                }
            }
        }
    }
}
