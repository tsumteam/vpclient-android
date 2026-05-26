package ru.mercury.vpclient.shared.domain.mapper

import ru.mercury.vpclient.shared.data.network.response.CurrentUserResponse

private const val DEFAULT_CLIENT_NAME = "Клиент"

val CurrentUserResponse.clientFullName: String
    get() {
        return listOf(clientName, clientMiddleName, clientSurname)
            .mapNotNull { value -> value?.trim()?.takeIf { it.isNotEmpty() } }
            .joinToString(" ")
            .ifEmpty { DEFAULT_CLIENT_NAME }
    }

val CurrentUserResponse.isFeminine: Boolean
    get() {
        return gender == CurrentUserResponse.GENDER_FEMININE
    }
