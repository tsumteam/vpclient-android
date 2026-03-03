package ru.mercury.vpclient.core.ui.transformation

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import ru.mercury.vpclient.core.ktx.normalizePhoneInput

class PhoneInputTransformation(
    private val maxDigits: Int = 13
): InputTransformation {

    override fun TextFieldBuffer.transformInput() {
        val normalizedPhone = normalizePhoneInput(asCharSequence().toString(), maxDigits)
        if (normalizedPhone != asCharSequence().toString()) {
            replace(0, length, normalizedPhone)
        }
    }
}
