package ru.mercury.vpclient.shared.ui.transformation

import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.insert

class PhoneOutputTransformation: OutputTransformation {
    override fun TextFieldBuffer.transformOutput() {
        val rawLength = length
        if (rawLength == 0) return
        insert(0, "+")
        if (rawLength > 1) insert(2, " (")
        if (rawLength > 4) insert(7, ") ")
        if (rawLength > 7) insert(12, "-")
        if (rawLength > 9) insert(15, "-")
    }
}
