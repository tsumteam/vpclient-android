package ru.mercury.vpclient.shared.data.error

data class ChangeFittingLineSizeException(
    override val message: String
): ClientException(message)
