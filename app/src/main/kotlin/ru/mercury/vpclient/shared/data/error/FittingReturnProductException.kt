package ru.mercury.vpclient.shared.data.error

data class FittingReturnProductException(
    override val message: String
): ClientException(message)
