package ru.mercury.vpclient.shared.ui.transformation

import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import ru.mercury.vpclient.shared.ktx.normalizePhoneInput

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
