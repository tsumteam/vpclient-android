package ru.mercury.vpclient.core.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import ru.mercury.vpclient.core.ui.theme.VPClientIcons
import ru.mercury.vpclient.core.ui.theme.VPClientTypography

@Composable
fun VPClientSecureTextField(
    state: TextFieldState,
    label: String,
    isErrorVisible: Boolean,
    error: String,
    isPasswordVisible: Boolean,
    onPasswordVisibilityClick: () -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        keyboardType = KeyboardType.Password,
        imeAction = ImeAction.Done
    ),
    onImeAction: (() -> Unit)? = null
) {
    val keyboardActionHandler = KeyboardActionHandler { performDefaultAction ->
        performDefaultAction()
        onImeAction?.invoke()
    }

    SecureTextField(
        state = state,
        label = { Text(text = label) },
        isError = isErrorVisible,
        trailingIcon = {
            when {
                isErrorVisible -> {
                    Icon(
                        painter = painterResource(VPClientIcons.Error),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
                else -> {
                    VPClientAnimatedVisibility(
                        visible = state.text.isNotEmpty()
                    ) {
                        IconButton(
                            onClick = onPasswordVisibilityClick
                        ) {
                            Icon(
                                painter = painterResource(if (isPasswordVisible) VPClientIcons.VisibilityOff else VPClientIcons.Visibility),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                }
            }
        },
        supportingText = if (isErrorVisible) {
            {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {
            null
        },
        modifier = modifier,
        textStyle = VPClientTypography.Medium_16_OnBackground,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = keyboardActionHandler,
        textObfuscationMode = if (isPasswordVisible) TextObfuscationMode.Visible else TextObfuscationMode.Hidden,
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            errorTextColor = MaterialTheme.colorScheme.onBackground,
            cursorColor = MaterialTheme.colorScheme.onBackground,
            errorCursorColor = MaterialTheme.colorScheme.onBackground,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            errorContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            errorLabelColor = MaterialTheme.colorScheme.error,
            errorSupportingTextColor = MaterialTheme.colorScheme.error
        )
    )
}
