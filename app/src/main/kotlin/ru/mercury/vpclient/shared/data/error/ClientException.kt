package ru.mercury.vpclient.shared.data.error

open class ClientException(
    override val message: String
): Exception(message)
