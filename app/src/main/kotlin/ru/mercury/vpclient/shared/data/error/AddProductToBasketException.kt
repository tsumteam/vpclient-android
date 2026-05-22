package ru.mercury.vpclient.shared.data.error

data class AddProductToBasketException(
    override val message: String
): ClientException(message)
