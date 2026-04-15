package ru.mercury.vpclient.shared.data.error

data class LogoutException(
    override val message: String
): ClientException(message)
