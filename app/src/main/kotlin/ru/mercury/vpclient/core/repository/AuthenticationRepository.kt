package ru.mercury.vpclient.core.repository

interface AuthenticationRepository {

    suspend fun register(phone: String, name: String): String

    suspend fun login(phone: String): String

    suspend fun continueLogin(phone: String, code: String): String

    suspend fun logout()
}
