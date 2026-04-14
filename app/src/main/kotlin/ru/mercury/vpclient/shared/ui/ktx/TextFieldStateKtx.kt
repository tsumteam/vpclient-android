package ru.mercury.vpclient.shared.ui.ktx

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun rememberSyncedTextFieldState(
    value: String,
    onValueChange: (String) -> Unit
): TextFieldState {
    val textState = rememberTextFieldState(initialText = value)

    LaunchedEffect(textState) {
        snapshotFlow { textState.text.toString() }
            .distinctUntilChanged()
            .collect { onValueChange(it) }
    }

    LaunchedEffect(value) {
        if (textState.text.toString() != value) {
            textState.setTextAndPlaceCursorAtEnd(value)
        }
    }

    return textState
}
