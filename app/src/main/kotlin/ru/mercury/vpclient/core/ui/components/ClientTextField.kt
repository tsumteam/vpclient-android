package ru.mercury.vpclient.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.text.input.InputTransformation
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.mercury.vpclient.core.ui.theme.ClientIcons
import ru.mercury.vpclient.core.ui.theme.ClientTheme
import ru.mercury.vpclient.core.ui.theme.green
import ru.mercury.vpclient.core.ui.theme.medium16
import ru.mercury.vpclient.core.ui.theme.onBackground

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
fun ClientTextField(
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
    val errorColor by animateColorAsState(
        targetValue = if (isErrorVisible) MaterialTheme.colorScheme.error else Color.Transparent,
        animationSpec = tween(durationMillis = 150),
        label = "client_text_field_error_border_color"
    )
    var normalHeightPx by remember { mutableIntStateOf(0) }

    TextField(
        state = textState,
        modifier = modifier
            .fillMaxWidth()
            .onSizeChanged { size ->
                if (!isErrorVisible) {
                    normalHeightPx = size.height
                }
            }
            .drawWithContent {
                drawContent()
                if (errorColor.alpha > 0f) {
                    val strokeWidth = 1.dp.toPx()
                    val cornerRadius = 8.dp.toPx()
                    val fallbackHeight = (size.height - 20.dp.toPx()).coerceAtLeast(0F)
                    val borderHeight = normalHeightPx.takeIf { it > 0 }?.toFloat() ?: fallbackHeight
                    drawRoundRect(
                        color = errorColor,
                        size = Size(width = size.width, height = borderHeight),
                        cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                        style = Stroke(width = strokeWidth)
                    )
                }
            },
        textStyle = MaterialTheme.typography.medium16.onBackground(),
        label = {
            Text(
                text = label
            )
        },
        trailingIcon = {
            when {
                isErrorVisible && currentValue.isNotEmpty() -> {
                    Icon(
                        painter = painterResource(ClientIcons.Close),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                isFocused && currentValue.isNotEmpty() -> {
                    IconButton(
                        onClick = { textState.setTextAndPlaceCursorAtEnd("") }
                    ) {
                        Icon(
                            painter = painterResource(ClientIcons.Close),
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

@Composable
fun ClientTextField(
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
        textStyle = MaterialTheme.typography.medium16.onBackground(),
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
                        painter = painterResource(ClientIcons.Close),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.green
                    )
                }
                isFocused && currentValue.isNotEmpty() -> {
                    IconButton(
                        onClick = { textState.setTextAndPlaceCursorAtEnd("") }
                    ) {
                        Icon(
                            painter = painterResource(ClientIcons.Close),
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
fun ClientTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isErrorVisible: Boolean,
    error: String,
    modifier: Modifier,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    inputTransformation: InputTransformation? = null,
    outputTransformation: OutputTransformation? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState().value
    val textState = rememberSyncedTextFieldState(value, onValueChange)
    val currentValue = textState.text.toString()
    val errorColor by animateColorAsState(
        targetValue = if (isErrorVisible) MaterialTheme.colorScheme.error else Color.Transparent,
        animationSpec = tween(durationMillis = 150),
        label = "client_text_field_with_transformation_error_border_color"
    )
    var normalHeightPx by remember { mutableIntStateOf(0) }

    TextField(
        state = textState,
        modifier = modifier
            .fillMaxWidth()
            .onSizeChanged { size ->
                if (!isErrorVisible) {
                    normalHeightPx = size.height
                }
            }
            .drawWithContent {
                drawContent()
                if (errorColor.alpha > 0f) {
                    val strokeWidth = 1.dp.toPx()
                    val cornerRadius = 8.dp.toPx()
                    val fallbackHeight = (size.height - 20.dp.toPx()).coerceAtLeast(0F)
                    val borderHeight = normalHeightPx.takeIf { it > 0 }?.toFloat() ?: fallbackHeight
                    drawRoundRect(
                        color = errorColor,
                        size = Size(width = size.width, height = borderHeight),
                        cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                        style = Stroke(width = strokeWidth)
                    )
                }
            },
        textStyle = MaterialTheme.typography.medium16.onBackground(),
        label = {
            Text(
                text = label
            )
        },
        trailingIcon = {
            if (isFocused && currentValue.isNotEmpty()) {
                IconButton(
                    onClick = { textState.setTextAndPlaceCursorAtEnd("") }
                ) {
                    Icon(
                        painter = painterResource(ClientIcons.Close),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
        inputTransformation = inputTransformation,
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
private fun ClientTextFieldPreview() {
    ClientTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            ClientTextField(
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
private fun ClientTextFieldPreview2() {
    ClientTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            ClientTextField(
                value = "",
                accepted = true,
                onValueChange = {},
                placeholder = "Введите сумму",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
