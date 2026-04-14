package ru.mercury.vpclient.features.login.intent

import ru.mercury.vpclient.shared.mvi.Intent

sealed interface LoginIntent: Intent {
    data object LoginClick: LoginIntent
    data object OnKeyboardDone: LoginIntent
    data class EnterPhone(val phone: String): LoginIntent
}
