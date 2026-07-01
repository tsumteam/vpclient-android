package ru.mercury.vpclient.shared.data.network.error

open class ClientException(
    override val message: String
): Exception(message)
