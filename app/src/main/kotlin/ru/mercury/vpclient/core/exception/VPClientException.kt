package ru.mercury.vpclient.core.exception

open class VPClientException(
    override val message: String
): Exception(message)
