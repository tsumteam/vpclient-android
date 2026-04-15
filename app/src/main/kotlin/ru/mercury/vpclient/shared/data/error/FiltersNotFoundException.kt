package ru.mercury.vpclient.shared.data.error

data class FiltersNotFoundException(
    override val message: String = "Фильтры не найдены"
): ClientException(message)
