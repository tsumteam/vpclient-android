package ru.mercury.vpclient.shared.data.error

data class SetProductSizeException(
    override val message: String
): ClientException(message)
