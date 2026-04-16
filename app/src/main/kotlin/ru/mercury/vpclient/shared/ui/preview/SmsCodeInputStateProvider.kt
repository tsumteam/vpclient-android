package ru.mercury.vpclient.shared.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import ru.mercury.vpclient.shared.data.entity.SmsCodeInputState

class SmsCodeInputStateProvider: PreviewParameterProvider<SmsCodeInputState> {
    override val values: Sequence<SmsCodeInputState> = sequenceOf(
        SmsCodeInputState(value = "", isErrorVisible = false),
        SmsCodeInputState(value = "123", isErrorVisible = false),
        SmsCodeInputState(value = "123456", isErrorVisible = false),
        SmsCodeInputState(value = "", isErrorVisible = true),
        SmsCodeInputState(value = "123", isErrorVisible = true),
        SmsCodeInputState(value = "123456", isErrorVisible = true)
    )
}
