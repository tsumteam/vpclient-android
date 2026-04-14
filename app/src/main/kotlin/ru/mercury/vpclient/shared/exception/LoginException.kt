package ru.mercury.vpclient.shared.exception

data class LoginException(
    override val message: String
): ClientException(message)
