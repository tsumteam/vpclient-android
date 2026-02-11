package ru.mercury.vpclient.core.exception

data class LoginException(
    override val message: String
): ClientException(message)
