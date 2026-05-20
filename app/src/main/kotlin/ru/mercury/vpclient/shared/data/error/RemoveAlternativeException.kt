package ru.mercury.vpclient.shared.data.error

data class RemoveAlternativeException(
    override val message: String
): ClientException(message)
