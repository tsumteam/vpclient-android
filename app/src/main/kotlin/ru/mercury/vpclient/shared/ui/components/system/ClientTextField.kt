package ru.mercury.vpclient.shared.ui.components.system

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.icons.Close24
import ru.mercury.vpclient.shared.ui.ktx.keyboardActionHandler
import ru.mercury.vpclient.shared.ui.ktx.rememberSyncedTextFieldState
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.regular12
import ru.mercury.vpclient.shared.ui.theme.regular15
import ru.mercury.vpclient.shared.ui.theme.surface3

@Composable
fun ClientTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isErrorVisible: Boolean = false,
    error: String = "",
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
            .heightIn(min = 52.dp)
            .onSizeChanged { size ->
                if (!isErrorVisible) {
                    normalHeightPx = size.height
                }
            }
            .drawWithContent {
                drawContent()
                if (errorColor.alpha > 0F) {
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
        textStyle = MaterialTheme.typography.regular15.copy(
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 19.sp,
            letterSpacing = .2.sp
        ),
        label = {
            Text(
                text = label
            )
        },
        trailingIcon = {
            ClientAnimatedVisibility(
                visible = isFocused && currentValue.isNotEmpty()
            ) {
                IconButton(
                    onClick = { textState.setTextAndPlaceCursorAtEnd("") }
                ) {
                    Icon(
                        imageVector = Close24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        },
        supportingText = when {
            isErrorVisible -> {
                {
                    Text(
                        text = error,
                        style = MaterialTheme.typography.regular12.copy(
                            color = MaterialTheme.colorScheme.error,
                            lineHeight = 16.sp,
                            letterSpacing = .2.sp
                        )
                    )
                }
            }
            else -> null
        },
        isError = isErrorVisible,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = keyboardActionHandler(keyboardOptions, keyboardActions),
        lineLimits = TextFieldLineLimits.SingleLine,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors().copy(
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            cursorColor = MaterialTheme.colorScheme.error,
            focusedContainerColor = MaterialTheme.colorScheme.surface3,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface3,
            errorContainerColor = MaterialTheme.colorScheme.surface3,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            errorSupportingTextColor = MaterialTheme.colorScheme.error,
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
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = enabled,
        textStyle = MaterialTheme.typography.regular15.copy(
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 19.sp,
            letterSpacing = .2.sp
        ),
        placeholder = {
            Text(
                text = placeholder,
                style = placeholderTextStyle
            )
        },
        trailingIcon = {
            ClientAnimatedVisibility(
                visible = isFocused && currentValue.isNotEmpty()
            ) {
                IconButton(
                    onClick = { textState.setTextAndPlaceCursorAtEnd("") }
                ) {
                    Icon(
                        imageVector = Close24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
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
            cursorColor = MaterialTheme.colorScheme.error,
            focusedContainerColor = MaterialTheme.colorScheme.surface3,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface3,
            disabledContainerColor = MaterialTheme.colorScheme.surface3,
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
            .heightIn(min = 52.dp)
            .onSizeChanged { size ->
                if (!isErrorVisible) {
                    normalHeightPx = size.height
                }
            }
            .drawWithContent {
                drawContent()
                if (errorColor.alpha > 0F) {
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
        textStyle = MaterialTheme.typography.regular15.copy(
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 19.sp,
            letterSpacing = .2.sp
        ),
        label = {
            Text(
                text = label
            )
        },
        trailingIcon = {
            ClientAnimatedVisibility(
                visible = isFocused && currentValue.isNotEmpty()
            ) {
                IconButton(
                    onClick = { textState.setTextAndPlaceCursorAtEnd("") }
                ) {
                    Icon(
                        imageVector = Close24,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
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
                        style = MaterialTheme.typography.regular12.copy(
                            color = MaterialTheme.colorScheme.error,
                            lineHeight = 16.sp,
                            letterSpacing = .2.sp
                        )
                    )
                }
            }
            else -> null
        },
        isError = isErrorVisible,
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
            cursorColor = MaterialTheme.colorScheme.error,
            focusedContainerColor = MaterialTheme.colorScheme.surface3,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface3,
            errorContainerColor = MaterialTheme.colorScheme.surface3,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            errorSupportingTextColor = MaterialTheme.colorScheme.error,
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
                .background(color = MaterialTheme.colorScheme.background),
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
private fun ClientTextField2Preview() {
    ClientTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface),
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
