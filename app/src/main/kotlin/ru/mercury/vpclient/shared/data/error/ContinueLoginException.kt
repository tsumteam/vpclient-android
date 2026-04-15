package ru.mercury.vpclient.shared.data.error

data class ContinueLoginException(
    override val message: String
): ClientException(message)
