package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.data.entity.SizeState

class SizeStateProvider: PreviewParameterProvider<SizeState> {
    override val values: Sequence<SizeState> = sequenceOf(
        SizeState(
            topText = "RU 36",
            bottomText = "IT 34",
            selected = false,
            enabled = true
        ),
        SizeState(
            topText = "RU 36",
            bottomText = "IT 34",
            selected = true,
            enabled = true
        ),
        SizeState(
            topText = "RU 36",
            bottomText = "IT 34",
            selected = false,
            enabled = false
        ),
        SizeState(
            topText = "RU 36",
            bottomText = "IT 34",
            selected = true,
            enabled = false
        )
    )
}
