package ru.mercury.vpclient.shared.exception

open class ClientException(
    override val message: String
): Exception(message)
