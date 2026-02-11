package ru.mercury.vpclient.features.register.intent

import ru.mercury.vpclient.core.mvi.Intent

sealed interface RegisterIntent: Intent {
    data object RegisterClick: RegisterIntent
    data object MoveFocusDown: RegisterIntent
    data object OnKeyboardDone: RegisterIntent
    data object OpenAgreement: RegisterIntent
    data class EnterName(val name: String): RegisterIntent
    data class EnterPhone(val phone: String): RegisterIntent
}
