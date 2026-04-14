package ru.mercury.vpclient.shared.exception

data class LogoutException(
    override val message: String
): ClientException(message)
