package ru.mercury.vpclient.shared.ui.transformation

import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.insert
import ru.mercury.vpclient.shared.data.FORMAT_RUB

class GiftCardAmountOutputTransformation: OutputTransformation {

    override fun TextFieldBuffer.transformOutput() {
        val rawLength = length
        if (rawLength == 0) return
        var insertionIndex = rawLength - 3
        while (insertionIndex > 0) {
            insert(insertionIndex, " ")
            insertionIndex -= 3
        }
        append(FORMAT_RUB.removePrefix("%s"))
    }
}
