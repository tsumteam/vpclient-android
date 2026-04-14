package ru.mercury.vpclient.shared.exception

data class FiltersNotFoundException(
    override val message: String = "Фильтры не найдены"
): ClientException(message)
