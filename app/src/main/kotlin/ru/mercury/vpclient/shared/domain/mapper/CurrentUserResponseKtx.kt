package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.PREFIX_SPACE
import ru.mercury.vpclient.shared.data.network.response.CurrentUserResponse
import ru.mercury.vpclient.shared.data.network.type.GenderType

private const val DEFAULT_CLIENT_NAME = "Клиент"

val CurrentUserResponse.clientFullName: String
    get() = listOf(clientName, clientSurname, clientMiddleName)
            .mapNotNull { value -> value?.trim()?.takeIf { it.isNotEmpty() } }
            .joinToString(PREFIX_SPACE)
            .ifEmpty { DEFAULT_CLIENT_NAME }

val CurrentUserResponse.isFeminine: Boolean
    get() = gender == GenderType.FEMININE
