package ru.mercury.vpclient.shared.data.error

data class DeleteProductException(
    override val message: String
): ClientException(message)
