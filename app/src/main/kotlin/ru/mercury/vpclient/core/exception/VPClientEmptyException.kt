package ru.mercury.vpclient.core.exception

data class VPClientEmptyException(
    override val message: String = "Данные отсутствуют"
): VPClientException(message)
