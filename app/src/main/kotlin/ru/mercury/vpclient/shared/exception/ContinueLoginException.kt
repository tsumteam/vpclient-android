package ru.mercury.vpclient.shared.exception

data class ContinueLoginException(
    override val message: String
): ClientException(message)
