package ru.mercury.vpclient.core.repository

import kotlinx.coroutines.flow.Flow
import ru.mercury.vpclient.core.persistence.database.entity.ClientEntity

interface AuthenticationRepository {

    val clientEntityFlow: Flow<ClientEntity>

    suspend fun register(phone: String, name: String)

    suspend fun login(phone: String)

    suspend fun continueLogin(code: String)

    suspend fun logout()

    suspend fun resendCode()
}
