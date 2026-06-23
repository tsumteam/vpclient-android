package ru.mercury.vpclient.shared.data.error

data class MyEmployeesException(
    override val message: String
): ClientException(message)
