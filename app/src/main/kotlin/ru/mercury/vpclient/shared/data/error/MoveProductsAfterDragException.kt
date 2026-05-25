package ru.mercury.vpclient.shared.data.error

data class MoveProductsAfterDragException(
    override val message: String
): ClientException(message)
