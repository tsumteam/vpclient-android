package ru.mercury.vpclient.shared.data.error

data class BasketShowAlternativesException(
    override val message: String
): ClientException(message)
