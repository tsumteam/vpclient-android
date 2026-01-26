package ru.mercury.vpclient.features.authentication.model

import kotlinx.coroutines.Job
import ru.mercury.vpclient.core.entity.LoginFailure
import ru.mercury.vpclient.core.entity.Password
import ru.mercury.vpclient.core.entity.Username
import ru.mercury.vpclient.core.mvi.Model

data class AuthenticationModel(
    val username: Username = Username(""),
    val password: Password = Password(""),
    val isPasswordVisible: Boolean = false,
    val authJob: Job? = null,
    val failure: LoginFailure? = null
): Model {

    val isAuthJobActive: Boolean
        get() = authJob != null && authJob.isActive

    val isLoginErrorVisible: Boolean
        get() = username.value.isEmpty() && (failure is LoginFailure.EmptyUsername || failure is LoginFailure.EmptyUsernameAndPassword)

    val isPasswordErrorVisible: Boolean
        get() = password.value.isEmpty() && (failure is LoginFailure.EmptyPassword || failure is LoginFailure.EmptyUsernameAndPassword)
}
