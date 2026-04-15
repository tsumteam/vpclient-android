package ru.mercury.vpclient.shared.data.error

data class ClientEmptyException(
    override val message: String = "Данные отсутствуют"
): ClientException(message)
