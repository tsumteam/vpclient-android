package ru.mercury.vpclient.features.code.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface CodeIntent: Intent {
    data object CollectClientEntity: CodeIntent
    data object StartResendTimerTicker: CodeIntent
    data object ConfirmClick: CodeIntent
    data object ResendCodeClick: CodeIntent
    data object OnKeyboardDone: CodeIntent
    data class EnterCode(val code: String): CodeIntent
}
