package ru.mercury.vpclient.core.exception

data class FiltersNotSupportedException(
    override val message: String = "Неподдерживаемый тип фильтра"
): ClientException(message)
