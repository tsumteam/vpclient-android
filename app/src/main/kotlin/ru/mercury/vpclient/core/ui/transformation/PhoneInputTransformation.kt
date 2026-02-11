package ru.mercury.vpclient.core.ui.transformation

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer

class PhoneInputTransformation(
    private val maxDigits: Int = 15
): InputTransformation {

    override fun TextFieldBuffer.transformInput() {
        val digitsOnly = asCharSequence()
            .filter(Char::isDigit)
            .take(maxDigits)
            .toString()
        if (digitsOnly != asCharSequence().toString()) {
            replace(0, length, digitsOnly)
        }
    }
}
