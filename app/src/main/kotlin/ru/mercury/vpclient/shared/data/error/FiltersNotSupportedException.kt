package ru.mercury.vpclient.shared.data.error

data class FiltersNotSupportedException(
    override val message: String = "Неподдерживаемый тип фильтра"
): ClientException(message)
