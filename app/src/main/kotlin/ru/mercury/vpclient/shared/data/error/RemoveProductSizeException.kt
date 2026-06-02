package ru.mercury.vpclient.shared.data.error

data class RemoveProductSizeException(
    override val message: String
): ClientException(message)
