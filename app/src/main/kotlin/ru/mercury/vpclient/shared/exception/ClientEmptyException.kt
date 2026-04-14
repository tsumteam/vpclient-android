package ru.mercury.vpclient.shared.exception

data class ClientEmptyException(
    override val message: String = "Данные отсутствуют"
): ClientException(message)
