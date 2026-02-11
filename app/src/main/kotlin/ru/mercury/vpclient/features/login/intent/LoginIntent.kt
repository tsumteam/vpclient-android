package ru.mercury.vpclient.features.login.intent

import ru.mercury.vpclient.core.mvi.Intent

sealed interface LoginIntent: Intent {
    data object LoginClick: LoginIntent
    data object OnKeyboardDone: LoginIntent
    data object OpenAgreement: LoginIntent
    data class EnterPhone(val phone: String): LoginIntent
}
