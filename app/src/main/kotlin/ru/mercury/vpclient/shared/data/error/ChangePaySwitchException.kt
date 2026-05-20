package ru.mercury.vpclient.shared.data.error

data class ChangePaySwitchException(
    override val message: String
): ClientException(message)
