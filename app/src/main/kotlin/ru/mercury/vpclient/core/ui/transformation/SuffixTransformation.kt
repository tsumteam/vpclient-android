package ru.mercury.vpclient.core.ui.transformation

import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer

class SuffixTransformation(
    private val suffix: String
): OutputTransformation {

    override fun TextFieldBuffer.transformOutput() {
        if (suffix.isNotEmpty()) {
            append(suffix)
        }
    }
}
