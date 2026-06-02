package ru.mercury.vpclient.shared.data.error

data class AddProductSizeException(
    override val message: String
): ClientException(message)
