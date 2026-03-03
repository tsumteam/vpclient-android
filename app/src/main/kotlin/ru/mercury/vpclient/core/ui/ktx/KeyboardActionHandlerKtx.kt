package ru.mercury.vpclient.core.ui.ktx

import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.ui.text.input.ImeAction

fun keyboardActionHandler(
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions
): KeyboardActionHandler {
    return KeyboardActionHandler { performDefaultAction ->
        val scope = object: KeyboardActionScope {
            override fun defaultKeyboardAction(imeAction: ImeAction) {
                performDefaultAction()
            }
        }
        val action = when (keyboardOptions.imeAction) {
            ImeAction.Done -> keyboardActions.onDone
            ImeAction.Go -> keyboardActions.onGo
            ImeAction.Next -> keyboardActions.onNext
            ImeAction.Previous -> keyboardActions.onPrevious
            ImeAction.Search -> keyboardActions.onSearch
            ImeAction.Send -> keyboardActions.onSend
            else -> null
        }
        when {
            action != null -> action.invoke(scope)
            else -> performDefaultAction()
        }
    }
}
