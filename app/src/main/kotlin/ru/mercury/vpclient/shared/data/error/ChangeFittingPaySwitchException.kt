package ru.mercury.vpclient.shared.data.error

data class ChangeFittingPaySwitchException(
    override val message: String
): ClientException(message)
