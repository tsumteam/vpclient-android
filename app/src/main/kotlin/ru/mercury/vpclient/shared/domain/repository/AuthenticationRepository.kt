package ru.mercury.vpclient.shared.domain.repository

import ru.mercury.vpclient.shared.data.network.response.CurrentUserResponse

interface AuthenticationRepository {

    suspend fun currentUser(): CurrentUserResponse

    suspend fun userId(): String

    suspend fun deleteProfile()

    suspend fun logout()
}
