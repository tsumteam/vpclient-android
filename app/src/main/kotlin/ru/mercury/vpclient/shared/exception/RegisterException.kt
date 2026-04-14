package ru.mercury.vpclient.shared.exception

data class RegisterException(
    override val message: String
): ClientException(message)
