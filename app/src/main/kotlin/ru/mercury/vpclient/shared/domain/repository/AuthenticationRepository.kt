package ru.mercury.vpclient.shared.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.shared.data.network.response.CurrentUserResponse
import ru.mercury.vpclient.shared.data.persistence.database.entity.ClientEntity

interface AuthenticationRepository {

    val clientEntityFlow: Flow<ClientEntity>

    suspend fun login(phone: String)

    suspend fun continueLogin(code: String)

    suspend fun currentUser(): CurrentUserResponse

    suspend fun userId(): String

    suspend fun deleteProfile()

    suspend fun logout()

    suspend fun resendCode()
}
