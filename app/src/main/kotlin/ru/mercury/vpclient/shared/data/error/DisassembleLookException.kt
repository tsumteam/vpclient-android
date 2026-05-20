package ru.mercury.vpclient.shared.data.error

data class DisassembleLookException(
    override val message: String
): ClientException(message)
