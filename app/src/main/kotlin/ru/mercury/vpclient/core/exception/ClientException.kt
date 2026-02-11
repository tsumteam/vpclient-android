package ru.mercury.vpclient.core.exception

open class ClientException(
    override val message: String
): Exception(message)
