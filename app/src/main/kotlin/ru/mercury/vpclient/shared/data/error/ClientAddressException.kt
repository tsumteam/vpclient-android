package ru.mercury.vpclient.shared.data.error

data class ClientAddressException(
    override val message: String
): ClientException(message)
