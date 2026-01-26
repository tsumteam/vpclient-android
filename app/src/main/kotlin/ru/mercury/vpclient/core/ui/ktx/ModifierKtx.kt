package ru.mercury.vpclient.core.ui.ktx

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerId
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.clickableWithoutRipple(
    block: () -> Unit
): Modifier = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        onClick = { block() }
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
