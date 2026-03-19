package ru.mercury.vpclient.core.exception

data class FiltersNotFoundException(
    override val message: String = "Фильтры не найдены"
): ClientException(message)
