package ru.mercury.vpclient.core.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.core.entity.SizeState
import ru.mercury.vpclient.core.entity.SizeSelectorState

class SizeSelectorStateProvider: PreviewParameterProvider<SizeSelectorState> {

    private val sizes = listOf(
        SizeState(topText = "RU 36", bottomText = "IT 34", selected = false, enabled = true),
        SizeState(topText = "RU 38", bottomText = "IT 36", selected = false, enabled = true),
        SizeState(topText = "RU 40", bottomText = "IT 38", selected = false, enabled = true),
        SizeState(topText = "RU 42", bottomText = "IT 40", selected = false, enabled = false),
        SizeState(topText = "RU 44", bottomText = "IT 42", selected = false, enabled = true),
        SizeState(topText = "RU 46", bottomText = "IT 44", selected = false, enabled = false)
    )

    override val values: Sequence<SizeSelectorState> = sequenceOf(
        SizeSelectorState(
            sizes = sizes,
            topText = "IT",
            bottomText = "RU",
            isSizeTableVisible = true
        ),
        SizeSelectorState(
            sizes = sizes.mapIndexed { index, state -> state.copy(selected = index == 1) },
            topText = "IT",
            bottomText = "RU",
            isSizeTableVisible = true
        ),
        SizeSelectorState(
            sizes = sizes.mapIndexed { index, state -> state.copy(selected = index == 3) },
            topText = "IT",
            bottomText = "RU",
            isSizeTableVisible = true
        )
    )
}
