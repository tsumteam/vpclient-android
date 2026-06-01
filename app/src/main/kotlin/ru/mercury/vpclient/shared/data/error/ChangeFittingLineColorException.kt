package ru.mercury.vpclient.shared.data.error

data class ChangeFittingLineColorException(
    override val message: String
): ClientException(message)
