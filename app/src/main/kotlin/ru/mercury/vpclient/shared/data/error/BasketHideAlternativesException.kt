package ru.mercury.vpclient.shared.data.error

data class BasketHideAlternativesException(
    override val message: String
): ClientException(message)
