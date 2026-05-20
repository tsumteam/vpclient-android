package ru.mercury.vpclient.shared.data.error

data class DeleteLookException(
    override val message: String
): ClientException(message)
