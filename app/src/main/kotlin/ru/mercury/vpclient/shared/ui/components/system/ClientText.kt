package ru.mercury.vpclient.shared.ui.components.system

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mercury.vpclient.shared.ui.theme.ClientStrings
import ru.mercury.vpclient.shared.ui.theme.ClientTheme

@Composable
fun ClientText(
    text: String,
    clickableRanges: List<IntRange>,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    clickableColor: Color = MaterialTheme.colorScheme.error,
    pressedBackgroundColor: Color = clickableColor.copy(alpha = .16F)
) {
    val normalizedRanges = clickableRanges.mapNotNull { range ->
        val start = range.first.coerceAtLeast(0)
        val endExclusive = (range.last + 1).coerceAtMost(text.length)
        if (start < endExclusive) start until endExclusive else null
    }
    var pressedRangeIndex by remember { mutableStateOf<Int?>(null) }
    val textLayoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val annotatedText = buildAnnotatedString {
        append(text)
        normalizedRanges.forEachIndexed { index, range ->
            addStyle(
                style = SpanStyle(
                    color = clickableColor,
                    background = if (pressedRangeIndex == index) pressedBackgroundColor else Color.Transparent
                ),
                start = range.first,
                end = range.last + 1
            )
        }
    }

    val clickableModifier = when {
        normalizedRanges.isEmpty() -> Modifier
        else -> Modifier.pointerInput(text, normalizedRanges) {
            detectTapGestures(
                onPress = { position ->
                    val layoutResult = textLayoutResult.value ?: return@detectTapGestures
                    val offset = layoutResult.getOffsetForPosition(position)
                    val clickableIndex = normalizedRanges.indexOfFirst { offset in it }
                    if (clickableIndex == -1) return@detectTapGestures
                    pressedRangeIndex = clickableIndex
                    val released = tryAwaitRelease()
                    pressedRangeIndex = null
                    if (released) onClick(clickableIndex)
                }
            )
        }
    }

    Text(
        text = annotatedText,
        modifier = modifier.then(clickableModifier),
        style = style,
        onTextLayout = { textLayoutResult.value = it }
    )
}

@Preview
@Composable
private fun ClientTextPreview() {
    ClientTheme {
        val text = stringResource(ClientStrings.LoginAgreementText)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
        ) {
            ClientText(
                text = stringResource(ClientStrings.LoginAgreementText),
                clickableRanges = listOf(65 until text.length),
                onClick = {},
                modifier = Modifier.padding(top = 24.dp),
            )
        }
    }
}
