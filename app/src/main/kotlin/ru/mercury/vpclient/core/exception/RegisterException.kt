package ru.mercury.vpclient.core.exception

data class RegisterException(
    override val message: String
): ClientException(message)
