package ru.mercury.vpclient.shared.data.error

data class SetProductColorException(
    override val message: String
): ClientException(message)
