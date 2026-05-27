package ru.mercury.vpclient.shared.data.error

data class ConfirmFittingException(
    override val message: String
): ClientException(message)
