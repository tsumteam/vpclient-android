package ru.mercury.vpclient.features.code.ui

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.data.CODE_LENGTH
import ru.mercury.vpclient.shared.data.entity.SmsCodeInputState
import ru.mercury.vpclient.shared.ui.preview.SmsCodeInputStateProvider
import ru.mercury.vpclient.shared.ui.theme.ClientTheme
import ru.mercury.vpclient.shared.ui.theme.black1
import ru.mercury.vpclient.shared.ui.theme.medium17
import ru.mercury.vpclient.shared.ui.theme.surface3

@Composable
fun SmsCodeInput(
    state: SmsCodeInputState,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val filteredValue = state.value.filter(Char::isDigit).take(CODE_LENGTH)
    val infiniteTransition = rememberInfiniteTransition(label = "sms_code_cursor_transition")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 1F,
        targetValue = 0F,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sms_code_cursor_alpha"
    )

    BasicTextField(
        value = filteredValue,
        onValueChange = { onValueChange(it.filter(Char::isDigit).take(CODE_LENGTH)) },
        singleLine = true,
        textStyle = TextStyle(color = Color.Transparent, fontSize = 1.sp),
        cursorBrush = SolidColor(Color.Transparent),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        ),
        keyboardActions = keyboardActions,
        modifier = modifier
            .focusRequester(focusRequester)
            .onFocusChanged {
                isFocused = it.isFocused
                if (it.isFocused) {
                    keyboardController?.show()
                }
            },
        decorationBox = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                },
                horizontalArrangement = Arrangement.spacedBy(9.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(CODE_LENGTH) { index ->
                    val symbol = filteredValue.getOrNull(index)?.toString().orEmpty()
                    val activeIndex = filteredValue.length.takeIf { it < CODE_LENGTH }
                    val shouldShowCursor = isFocused && activeIndex == index

                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surface3,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = if (state.isErrorVisible) MaterialTheme.colorScheme.error else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (symbol.isNotEmpty()) {
                            Text(
                                text = symbol,
                                style = MaterialTheme.typography.medium17.copy(
                                    color = MaterialTheme.colorScheme.black1
                                )
                            )
                        }

                        if (shouldShowCursor) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(width = 2.dp, height = 20.dp)
                                    .background(
                                        MaterialTheme.colorScheme.error.copy(
                                            alpha = cursorAlpha
                                        )
                                    )
                            )
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun SmsCodeInputPreview(
    @PreviewParameter(SmsCodeInputStateProvider::class) state: SmsCodeInputState
) {
    ClientTheme {
        SmsCodeInput(
            state = state,
            onValueChange = {},
            focusRequester = remember { FocusRequester() }
        )
    }
}
