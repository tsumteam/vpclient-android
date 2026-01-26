package ru.mercury.vpclient.features.authentication.intent

import ru.mercury.vpclient.core.mvi.Intent

sealed interface AuthenticationIntent: Intent {
    data object CollectAutofill: AuthenticationIntent
    data object LoginClick: AuthenticationIntent
    data object DismissAttention: AuthenticationIntent
    data object ClearFocus: AuthenticationIntent
    data object PasswordVisibilityClick: AuthenticationIntent
    data object DownFocus: AuthenticationIntent
    data class EnterUsername(val username: String): AuthenticationIntent
    data class EnterPassword(val password: String): AuthenticationIntent
}
