package ru.mercury.vpclient.core.exception

data class ContinueLoginException(
    override val message: String
): ClientException(message)
