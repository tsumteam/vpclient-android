package ru.mercury.vpclient.shared.data.error

data class MyEmployeeException(
    override val message: String
): ClientException(message)
