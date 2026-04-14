package ru.mercury.vpclient.shared.exception

data class FiltersNotSupportedException(
    override val message: String = "Неподдерживаемый тип фильтра"
): ClientException(message)
