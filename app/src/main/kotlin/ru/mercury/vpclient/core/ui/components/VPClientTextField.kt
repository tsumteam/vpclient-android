package ru.mercury.vpclient.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.mercury.vpclient.core.ui.theme.VPClientIcons
import ru.mercury.vpclient.core.ui.theme.VPClientTheme
import ru.mercury.vpclient.core.ui.theme.VPClientTypography
import ru.mercury.vpclient.core.ui.theme.green

@Composable
private fun rememberSyncedTextFieldState(
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

private fun keyboardActionHandler(
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions
): KeyboardActionHandler =
    KeyboardActionHandler { performDefaultAction ->
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

@Composable
fun VPClientTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isErrorVisible: Boolean,
    error: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value
    val textState = rememberSyncedTextFieldState(value, onValueChange)
    val currentValue = textState.text.toString()

    TextField(
        state = textState,
        modifier = modifier.fillMaxWidth(),
        textStyle = VPClientTypography.Medium_16_OnBackground,
        label = {
            Text(
                text = label
            )
        },
        trailingIcon = {
            when {
                isErrorVisible -> {
                    Icon(
                        painter = painterResource(VPClientIcons.Error),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                isFocused && currentValue.isNotEmpty() -> {
                    IconButton(
                        onClick = { textState.setTextAndPlaceCursorAtEnd("") }
                    ) {
                        Icon(
                            painter = painterResource(VPClientIcons.Clear),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        },
        supportingText = when {
            isErrorVisible -> {
                {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            else -> null
        },
        keyboardOptions = keyboardOptions,
        onKeyboardAction = keyboardActionHandler(keyboardOptions, keyboardActions),
        lineLimits = TextFieldLineLimits.SingleLine,
        interactionSource = interactionSource,
        colors = TextFieldDefaults.colors().copy(
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            cursorColor = MaterialTheme.colorScheme.onBackground,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
fun VPClientTextField(
    value: String,
    accepted: Boolean,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholderTextStyle: TextStyle = LocalTextStyle.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    outputTransformation: OutputTransformation? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value
    val textState = rememberSyncedTextFieldState(value, onValueChange)
    val currentValue = textState.text.toString()

    TextField(
        state = textState,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        textStyle = VPClientTypography.Medium_16_OnBackground,
        placeholder = {
            Text(
                text = placeholder,
                style = placeholderTextStyle
            )
        },
        trailingIcon = {
            when {
                accepted -> {
                    Icon(
                        painter = painterResource(VPClientIcons.Done),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.green
                    )
                }
                isFocused && currentValue.isNotEmpty() -> {
                    IconButton(
                        onClick = { textState.setTextAndPlaceCursorAtEnd("") }
                    ) {
                        Icon(
                            painter = painterResource(VPClientIcons.Clear),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        },
        outputTransformation = outputTransformation,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = keyboardActionHandler(keyboardOptions, keyboardActions),
        lineLimits = TextFieldLineLimits.SingleLine,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors().copy(
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            cursorColor = MaterialTheme.colorScheme.onBackground,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
fun VPClientTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isErrorVisible: Boolean,
    error: String,
    modifier: Modifier,
    trailingIcon: @Composable () -> Unit,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    outputTransformation: OutputTransformation? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val textState = rememberSyncedTextFieldState(value, onValueChange)

    TextField(
        state = textState,
        modifier = modifier.fillMaxWidth(),
        textStyle = VPClientTypography.Medium_16_OnBackground,
        label = {
            Text(
                text = label
            )
        },
        trailingIcon = trailingIcon,
        supportingText = when {
            isErrorVisible -> {
                {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            else -> null
        },
        outputTransformation = outputTransformation,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = keyboardActionHandler(keyboardOptions, keyboardActions),
        lineLimits = TextFieldLineLimits.SingleLine,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors().copy(
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            cursorColor = MaterialTheme.colorScheme.onBackground,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}



@Preview
@Composable
private fun VPClientTextFieldPreview() {
    VPClientTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            VPClientTextField(
                value = "",
                onValueChange = {},
                label = "Логин",
                isErrorVisible = true,
                error = "Введите логин",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun VPClientTextFieldPreview2() {
    VPClientTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            VPClientTextField(
                value = "",
                accepted = true,
                onValueChange = {},
                placeholder = "Введите сумму",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
