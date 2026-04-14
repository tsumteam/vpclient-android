package ru.mercury.vpclient.shared.entity

sealed interface LoginFailure {
    data object EmptyUsername: LoginFailure
    data object EmptyPassword: LoginFailure
    data object EmptyUsernameAndPassword: LoginFailure
    data class Network(val message: String?): LoginFailure
}
