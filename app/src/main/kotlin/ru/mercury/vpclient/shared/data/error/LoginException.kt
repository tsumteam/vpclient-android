package ru.mercury.vpclient.shared.data.error

data class LoginException(
    override val message: String
): ClientException(message)
