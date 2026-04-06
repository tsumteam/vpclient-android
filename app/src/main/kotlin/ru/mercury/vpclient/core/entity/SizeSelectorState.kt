package ru.mercury.vpclient.core.entity

data class SizeSelectorState(
    val sizes: List<SizeState>,
    val topText: String,
    val bottomText: String,
    val isSizeTableVisible: Boolean
) {
    companion object {
        val Empty = SizeSelectorState(
            sizes = emptyList(),
            topText = "",
            bottomText = "",
            isSizeTableVisible = false
        )
    }
}
