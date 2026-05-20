package ru.mercury.vpclient.shared.data.error

data class SwitchProductWithAlternativeException(
    override val message: String
): ClientException(message)
