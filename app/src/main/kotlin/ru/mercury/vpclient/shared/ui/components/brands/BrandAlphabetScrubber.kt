package ru.mercury.vpclient.shared.ui.components.brands

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewWrapper
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mercury.vpclient.shared.ui.preview.ThemeWrapper
import ru.mercury.vpclient.shared.ui.theme.regular12

data class BrandAlphabetScrubberState(
    val letters: List<String>,
    val onLetterSelect: (String) -> Unit
)

@Composable
fun BrandAlphabetScrubber(
    state: BrandAlphabetScrubberState,
    modifier: Modifier = Modifier
) {
    val hapticFeedback = LocalHapticFeedback.current
    var scrubberHeight by remember { mutableFloatStateOf(0F) }

    Column(
        modifier = modifier
            .width(20.dp)
            .wrapContentHeight()
            .onSizeChanged { size -> scrubberHeight = size.height.toFloat() }
            .pointerInput(state.letters) {
                awaitEachGesture {
                    var currentLetter: String? = null
                    fun selectLetter(y: Float) {
                        if (scrubberHeight == 0F || state.letters.isEmpty()) return
                        val index = (y / scrubberHeight * state.letters.size)
                            .toInt()
                            .coerceIn(0, state.letters.lastIndex)
                        val letter = state.letters[index]
                        if (letter != currentLetter) {
                            currentLetter = letter
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            state.onLetterSelect(letter)
                        }
                    }

                    val down = awaitFirstDown(requireUnconsumed = false)
                    selectLetter(down.position.y)
                    drag(down.id) { change ->
                        selectLetter(change.position.y)
                        change.consume()
                    }
                }
            },
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        state.letters.forEach { letter ->
            Text(
                text = letter,
                style = MaterialTheme.typography.regular12.copy(
                    color = MaterialTheme.colorScheme.error,
                    lineHeight = 16.sp,
                    letterSpacing = .2.sp,
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}

@PreviewWrapper(ThemeWrapper::class)
@Preview(showBackground = true)
@Composable
private fun BrandAlphabetScrubberPreview(
    @PreviewParameter(BrandAlphabetScrubberStateProvider::class) state: BrandAlphabetScrubberState
) {
    BrandAlphabetScrubber(
        state = state
    )
}

private class BrandAlphabetScrubberStateProvider: PreviewParameterProvider<BrandAlphabetScrubberState> {
    override val values: Sequence<BrandAlphabetScrubberState> = sequenceOf(
        BrandAlphabetScrubberState(
            letters = listOf("#") + ('A'..'Z').map { letter -> letter.toString() },
            onLetterSelect = {}
        ),
        BrandAlphabetScrubberState(
            letters = listOf("A", "B", "L", "M", "S", "V"),
            onLetterSelect = {}
        )
    )
}
