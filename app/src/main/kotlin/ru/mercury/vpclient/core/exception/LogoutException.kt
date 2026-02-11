package ru.mercury.vpclient.core.exception

data class LogoutException(
    override val message: String
): ClientException(message)
