package ru.mercury.vpclient.core.exception

data class ClientEmptyException(
    override val message: String = "Данные отсутствуют"
): ClientException(message)
