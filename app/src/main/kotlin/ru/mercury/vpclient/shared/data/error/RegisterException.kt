package ru.mercury.vpclient.shared.data.error

data class RegisterException(
    override val message: String
): ClientException(message)
