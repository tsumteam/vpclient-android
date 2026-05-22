package ru.mercury.vpclient.shared.data.error

data class BasketReturnOriginalException(
    override val message: String
): ClientException(message)
