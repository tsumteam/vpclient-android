package ru.mercury.vpclient.shared.data.error

data class SetProductQuantityException(
    override val message: String
): ClientException(message)
